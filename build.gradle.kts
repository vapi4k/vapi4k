import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.SourcesJar
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.dokka.gradle.DokkaExtension
import org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier

plugins {
    alias(libs.plugins.jvm) apply true
    alias(libs.plugins.dokka) apply true
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.maven.publish) apply false

    alias(libs.plugins.pambrose.stable.versions) apply true
    alias(libs.plugins.pambrose.kotlinter) apply false
}

val versionStr: String by extra

val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")

val jvmPluginId = libs.plugins.jvm.get().pluginId
val dokkaPluginId = libs.plugins.dokka.get().pluginId
val kotlinterPluginId = libs.plugins.pambrose.kotlinter.get().pluginId

allprojects {
    extra["versionStr"] = findProperty("overrideVersion")?.toString() ?: "1.7.0"
    extra["releaseDate"] = LocalDate.now().format(formatter)
    group = "com.vapi4k"
    version = versionStr

    repositories {
        google()
        mavenCentral()
    }
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

dokka {
    moduleName.set("vapi4k")
    dokkaPublications.html {
        outputDirectory.set(layout.buildDirectory.dir("kdocs"))
    }
    pluginsConfiguration.html {
        homepageLink.set("https://github.com/vapi4k/vapi4k")
        footerMessage.set("vapi4k")
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
        jvmToolchain(17)

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
        plugin("com.vanniktech.maven.publish")
    }

    extensions.configure<com.vanniktech.maven.publish.MavenPublishBaseExtension> {
        configure(
            com.vanniktech.maven.publish.KotlinJvm(
                javadocJar = JavadocJar.Dokka("dokkaGeneratePublicationHtml"),
                sourcesJar = SourcesJar.Sources(),
            ),
        )
        coordinates("com.vapi4k", project.name, version.toString())

        pom {
            name.set(project.name)
            description.set(provider { project.description })
            url.set("https://github.com/vapi4k/vapi4k")
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
                connection.set("scm:git:https://github.com/vapi4k/vapi4k.git")
                developerConnection.set("scm:git:ssh://github.com/vapi4k/vapi4k.git")
                url.set("https://github.com/vapi4k/vapi4k")
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
