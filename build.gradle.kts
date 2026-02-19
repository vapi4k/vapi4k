import java.time.LocalDate
import java.time.format.DateTimeFormatter
import org.jetbrains.dokka.gradle.DokkaExtension
import org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier

plugins {
    `java-library`
    `maven-publish`

    alias(libs.plugins.jvm) apply true
    alias(libs.plugins.dokka) apply true
    alias(libs.plugins.kotlin.serialization)

    alias(libs.plugins.pambrose.envvar) apply true
    alias(libs.plugins.pambrose.stable.versions) apply true
    alias(libs.plugins.pambrose.kotlinter) apply false
    alias(libs.plugins.pambrose.repos) apply false
    alias(libs.plugins.pambrose.snapshot) apply false
    alias(libs.plugins.pambrose.testing) apply false
}

val versionStr: String by extra

val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")

val kotlinLib = libs.plugins.jvm.get().toString().split(":").first()
val dokkaLib = libs.plugins.dokka.get().toString().split(":").first()
val ktlinterLib = libs.plugins.pambrose.kotlinter.get().toString().split(":").first()
val repoLib = libs.plugins.pambrose.repos.get().toString().split(":").first()
val snapshotLib = libs.plugins.pambrose.snapshot.get().toString().split(":").first()
val testingLib = libs.plugins.pambrose.testing.get().toString().split(":").first()

allprojects {
    apply {
        plugin(repoLib)
    }
    extra["versionStr"] = "1.3.9"
    extra["releaseDate"] = LocalDate.now().format(formatter)
    group = "com.github.vapi4k.vapi4k"
    version = versionStr
}

subprojects {
    tasks.withType<GenerateModuleMetadata> {
        enabled = false
    }

    apply {
        plugin("java-library")
        plugin("maven-publish")
        plugin(dokkaLib)
        plugin(testingLib)
        plugin(snapshotLib)
        plugin(ktlinterLib)
    }

    configureKotlin()
    configureDokka()
}

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
        plugin(kotlinLib)
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
