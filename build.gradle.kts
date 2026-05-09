import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.SourcesJar
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.dokka.gradle.DokkaExtension
import org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier
import java.time.LocalDate
import java.time.format.DateTimeFormatter

plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.maven.publish) apply false

    alias(libs.plugins.pambrose.stable.versions)
    alias(libs.plugins.pambrose.kotlinter) apply false
}

val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")

val jvmPluginId = libs.plugins.jvm.get().pluginId
val dokkaPluginId = libs.plugins.dokka.get().pluginId
val kotlinterPluginId = libs.plugins.pambrose.kotlinter.get().pluginId
val mavenPublishPluginId = libs.plugins.maven.publish.get().pluginId
val jvmVersion = libs.versions.jvm.get().toInt()

val moduleName = "vapi4k"
val projectUrl = "https://github.com/vapi4k/vapi4k"

allprojects {
    findProperty("overrideVersion")?.toString()?.let { version = it }
    extra["versionStr"] = version.toString()
    extra["releaseDate"] = LocalDate.now().format(formatter)
}

subprojects {
    apply {
        plugin("java-library")
        plugin(dokkaPluginId)
        plugin(kotlinterPluginId)
    }

    configureKotlin()
    configureDokka()
    if (project.name != "vapi4k-snippets") configurePublishing()
    configureTesting()
}

tasks.withType<PublishToMavenRepository>().configureEach { enabled = false }
tasks.withType<PublishToMavenLocal>().configureEach { enabled = false }

// Test configurations can't be resolved by ben-manes under Gradle 9 because the
// Kotlin Gradle Plugin tries to add dependency constraints to non-declarable
// configurations (testCompileClasspath/testRuntimeClasspath), which Gradle 9
// rejects. ben-manes catches and silently drops the whole config, so test-only
// deps (kotest, flyway, testcontainers, ktor-server-tests) vanish from the
// report instead of being flagged "unresolved". Skip them explicitly so the
// gap is intentional, not invisible.
tasks.withType<DependencyUpdatesTask>().configureEach {
    filterConfigurations = Spec { !it.name.startsWith("test") }
}

dokka {
    moduleName.set(moduleName)
    dokkaPublications.html {
        outputDirectory.set(layout.buildDirectory.dir("kdocs"))
    }
    pluginsConfiguration.html {
        homepageLink.set(projectUrl)
        footerMessage.set(moduleName)
    }
}

dependencies {
    dokka(project(":vapi4k-core"))
    dokka(project(":vapi4k-dbms"))
    dokka(project(":vapi4k-utils"))
}

fun Project.configureKotlin() {
    apply {
        plugin(jvmPluginId)
    }

    kotlin {
        jvmToolchain(jvmVersion)

        sourceSets.all {
            listOf(
                "kotlinx.serialization.ExperimentalSerializationApi",
                "kotlin.concurrent.atomics.ExperimentalAtomicApi",
                "kotlin.contracts.ExperimentalContracts",
            ).forEach { languageSettings.optIn(it) }
        }
    }
}

fun Project.configureDokka() {
    extensions.configure<DokkaExtension> {
        dokkaSourceSets.configureEach {
            enableKotlinStdLibDocumentationLink.set(false)
            enableJdkDocumentationLink.set(false)

            documentedVisibilities(VisibilityModifier.Public)

            // Exclude everything first
            perPackageOption {
                matchingRegex.set("com.vapi4k.*")
                suppress.set(true)
            }

            // Include specific packages
            perPackageOption {
                matchingRegex.set("com.vapi4k.api.*")
                reportUndocumented.set(false)
                skipDeprecated.set(false)
                suppress.set(false)
            }
        }
    }
}

fun Project.configurePublishing() {
    apply {
        plugin(dokkaPluginId)
        plugin(mavenPublishPluginId)
    }

    dokka {
        pluginsConfiguration.html {
            homepageLink.set(projectUrl)
        }
    }

    extensions.configure<com.vanniktech.maven.publish.MavenPublishBaseExtension> {
        configure(
            com.vanniktech.maven.publish.KotlinJvm(
                javadocJar = JavadocJar.Dokka("dokkaGeneratePublicationHtml"),
                sourcesJar = SourcesJar.Sources(),
            ),
        )
        coordinates(group.toString(), project.name, version.toString())

        pom {
            name.set(project.name)
            description.set(provider { project.description })
            url.set(projectUrl)
            licenses {
                license {
                    name.set("Apache License 2.0")
                    url.set("https://www.apache.org/licenses/LICENSE-2.0")
                }
            }
            developers {
                developer {
                    id.set("pambrose")
                    name.set("Paul Ambrose")
                    email.set("paul@pambrose.com")
                }
            }
            scm {
                connection.set("scm:git:$projectUrl.git")
                developerConnection.set("scm:git:ssh://github.com/vapi4k/vapi4k.git")
                url.set(projectUrl)
            }
        }

        publishToMavenCentral(automaticRelease = true)
        signAllPublications()
    }

    // Skip signing when no GPG key is provided (e.g., local publishing)
    tasks.withType<Sign>().configureEach {
        isEnabled = project.findProperty("signingInMemoryKey") != null
    }
}

fun Project.configureTesting() {
    tasks.withType<Test>().configureEach {
        useJUnitPlatform()

        testLogging {
            events("passed", "skipped", "failed", "standardOut", "standardError")
            exceptionFormat = TestExceptionFormat.FULL
            showStandardStreams = true
        }
    }
}
