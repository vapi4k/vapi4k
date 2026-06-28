import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.SourcesJar
import dev.detekt.gradle.Detekt
import dev.detekt.gradle.extensions.DetektExtension
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.dokka.gradle.DokkaExtension
import org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.maven.publish) apply false

    alias(libs.plugins.ben.manes.versions)
    alias(libs.plugins.pambrose.kotlinter) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.kover)
}

val jvmPluginId = libs.plugins.jvm.get().pluginId
val dokkaPluginId = libs.plugins.dokka.get().pluginId
val kotlinterPluginId = libs.plugins.pambrose.kotlinter.get().pluginId
val mavenPublishPluginId = libs.plugins.maven.publish.get().pluginId
val detektPluginId = libs.plugins.detekt.get().pluginId
val koverPluginId = libs.plugins.kover.get().pluginId
val jvmVersion = libs.versions.jvm.get().toInt()

val moduleName = "vapi4k"
val projectUrl = "https://github.com/vapi4k/vapi4k"

allprojects {
    findProperty("overrideVersion")?.toString()?.let { version = it }
}

subprojects {
    apply {
        plugin("java-library")
        plugin(kotlinterPluginId)
    }

    configureKotlin()
    if (project.name != "vapi4k-snippets") {
        apply { plugin(dokkaPluginId) }
        configureDokka()
        configurePublishing()
        configureDetekt()
        configureKover()
    }
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
// gap is intentional, not invisible, and remind the user at the end of the
// report to check those versions manually.
val testOnlyVersionKeys = listOf("kotest", "flyway", "testcontainers")
val libsCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")
tasks.withType<DependencyUpdatesTask>().configureEach {
    filterConfigurations = Spec { !it.name.startsWith("test") }

    doLast {
        val pad = testOnlyVersionKeys.maxOf { it.length }
        logger.lifecycle("")
        logger.lifecycle("Test-only dependencies not checked above (Gradle 9 + KGP limitation).")
        logger.lifecycle("Verify these versions manually against their release pages:")
        testOnlyVersionKeys.forEach { key ->
            val version = libsCatalog.findVersion(key).orElseThrow().requiredVersion
            logger.lifecycle("  ${key.padEnd(pad)}  $version")
        }
    }
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

    kover(project(":vapi4k-core"))
    kover(project(":vapi4k-dbms"))
    kover(project(":vapi4k-utils"))
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

        // Run the unused-return-value checker over production code only. Kotest's
        // assertion DSL (e.g. shouldBe) returns its receiver, and tests intentionally
        // discard that result, so applying the checker to the test source set would
        // emit only false-positive warnings.
        tasks.named<KotlinCompile>("compileKotlin") {
            compilerOptions {
                freeCompilerArgs.add("-Xreturn-value-checker=check")
            }
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
            name.set(
                project.name.split("-").joinToString(" ") { part ->
                    if (part.equals("dbms", ignoreCase = true)) "DBMS"
                    else part.replaceFirstChar { it.uppercaseChar() }
                },
            )
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
            events = setOf(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED, TestLogEvent.STANDARD_ERROR)
            exceptionFormat = TestExceptionFormat.FULL
            showStandardStreams = false
        }
    }
}

fun Project.configureDetekt() {
    apply { plugin(detektPluginId) }

    extensions.configure<DetektExtension> {
        buildUponDefaultConfig.set(true)
        allRules.set(false)
        ignoreFailures.set(false)
        config.setFrom(rootProject.files("config/detekt/detekt.yml"))
        source.setFrom(files("src/main/kotlin", "src/test/kotlin"))
    }

    tasks.withType<Detekt>().configureEach {
        jvmTarget.set(jvmVersion.toString())
        reports {
            html.required.set(true)
            checkstyle.required.set(true)
            sarif.required.set(false)
            markdown.required.set(false)
        }
    }
}

fun Project.configureKover() {
    apply { plugin(koverPluginId) }
}
