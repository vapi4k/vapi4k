plugins {
    alias(libs.plugins.jvm) apply false
    alias(libs.plugins.versions) apply true
    // alias(libs.plugins.kotlinter) apply false
    alias(libs.plugins.dokka) apply true
}

val versionStr: String by extra
val kotlinLib = libs.plugins.jvm.get().toString().split(":").first()
val dokkaLib = libs.plugins.dokka.get().toString().split(":").first()
// val ktlinterLib = libs.plugins.kotlinter.get().toString().split(":").first()

allprojects {
    extra["versionStr"] = "1.2.3"
    extra["releaseDate"] = "2/8/2025"
    group = "com.github.vapi4k"
    version = versionStr

    repositories {
        google()
        mavenCentral()
    }
}

subprojects {
    apply {
        plugin("java-library")
        plugin("maven-publish")
        plugin(kotlinLib)
        plugin(dokkaLib)
        // plugin(ktlinterLib)
    }

    tasks.withType<org.jetbrains.dokka.gradle.DokkaTask>().configureEach {
        dokkaSourceSets {
            configureEach {
                outputDirectory.set(layout.buildDirectory.dir("kdocs"))

                noStdlibLink.set(true)
                noJdkLink.set(true)

                documentedVisibilities.set(setOf(org.jetbrains.dokka.DokkaConfiguration.Visibility.PUBLIC))

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
