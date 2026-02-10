import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.dokka.DokkaConfiguration
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    `java-library`
    `maven-publish`

    alias(libs.plugins.jvm) apply true
    alias(libs.plugins.kotlinter) apply true
    alias(libs.plugins.versions) apply true
    alias(libs.plugins.dokka) apply true
}

val versionStr: String by extra

val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")

val kotlinLib = libs.plugins.jvm.get().toString().split(":").first()
val dokkaLib = libs.plugins.dokka.get().toString().split(":").first()
val ktlinterLib = libs.plugins.kotlinter.get().toString().split(":").first()

allprojects {
    extra["versionStr"] = "1.3.7"
    extra["releaseDate"] = LocalDate.now().format(formatter)
    group = "com.github.vapi4k"
    version = versionStr

    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

subprojects {
    apply {
        plugin("java-library")
        plugin("maven-publish")
        plugin(dokkaLib)
    }

    configureKotlin()
    configureTesting()
    configureKotlinter()
    configureDokka()
}

configureVersions()

dokka {
    moduleName.set("vapi4k")
    dokkaPublications.html {
        outputDirectory.set(layout.buildDirectory.dir("kdocs"))
    }
    pluginsConfiguration.html {
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
        plugin(kotlinLib)
    }

    configurations.all {
        resolutionStrategy.cacheChangingModulesFor(0, "seconds")
    }

    kotlin {
        jvmToolchain(17)

        sourceSets.all {
            languageSettings.optIn("kotlinx.serialization.ExperimentalSerializationApi")
            languageSettings.optIn("kotlin.concurrent.atomics.ExperimentalAtomicApi")
            languageSettings.optIn("kotlin.contracts.ExperimentalContracts")
        }
    }
}

fun Project.configureTesting() {
    tasks.test {
        useJUnitPlatform()

        testLogging {
            events("passed", "skipped", "failed", "standardOut", "standardError")
            exceptionFormat = TestExceptionFormat.FULL
            showStandardStreams = true
        }
    }
}

fun Project.configureVersions() {
    tasks {
        withType<DependencyUpdatesTask> {
            rejectVersionIf {
                listOf("-BETA", "-RC", "-M").any { candidate.version.uppercase().contains(it) }
            }
        }
    }
}

fun Project.configureKotlinter() {
    apply {
        plugin(ktlinterLib)
    }

    kotlinter {
        reporters = arrayOf("checkstyle", "plain")
    }
}

fun Project.configureDokka() {
    tasks.withType<DokkaTask>().configureEach {
        dokkaSourceSets {
            configureEach {
                outputDirectory.set(layout.buildDirectory.dir("kdocs"))

                noStdlibLink.set(true)
                noJdkLink.set(true)

                documentedVisibilities.set(setOf(DokkaConfiguration.Visibility.PUBLIC))

                // Exclude everything first
                perPackageOption {
                    matchingRegex.set("com.vapi4k.*")
                    suppress.set(true)
                }

                // Include specific packages
                perPackageOption {
                    matchingRegex.set("com.vapi4k.api.*")
                    includeNonPublic.set(false)
                    reportUndocumented.set(false)
                    skipDeprecated.set(false)
                    suppress.set(false)
                }
            }
        }
    }
}
