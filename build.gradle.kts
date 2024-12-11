plugins {
    alias(libs.plugins.jvm) apply false
    alias(libs.plugins.versions) apply true
    // alias(libs.plugins.kotlinter) apply false
    alias(libs.plugins.dokka) apply true
}

val versionStr: String by extra
val kotlinLib = libs.plugins.jvm.get().toString().split(":").first()
// val ktlinterLib = libs.plugins.kotlinter.get().toString().split(":").first()

allprojects {
    extra["versionStr"] = "1.2.1"
    extra["releaseDate"] = "12/11/2024"
    group = "com.github.vapi4k"
    version = versionStr

    repositories {
        google()
        mavenCentral()
//        maven("https://maven.pkg.jetbrains.space/public/p/ktor/eap")
    }
}

subprojects {
    apply {
        plugin("java")
        plugin("maven-publish")
        plugin(kotlinLib)
        // plugin(ktlinterLib)
    }

    tasks.withType<org.jetbrains.dokka.gradle.DokkaTaskPartial>().configureEach {
        dokkaSourceSets {
            configureEach {
//              "customAssets": ["${file("assets/my-image.png")}"],
//              "customStyleSheets": ["${file("assets/my-styles.css")}"],
//              "separateInheritedMembers": false,
//              "templatesDir": "${file("dokka/templates")}",
//              "mergeImplicitExpectActualDeclarations": false
                val dokkaBaseConfiguration = """
                {
                  "footerMessage": "Vapi4k"
                }
                """
                pluginsMapConfiguration.set(
                    mapOf(
                        // fully qualified plugin name to json configuration
                        "org.jetbrains.dokka.base.DokkaBase" to dokkaBaseConfiguration
                    )
                )

                // Include specific markdown files if needed
                // includes.from("packages.md")

                //displayName.set("Vapi4k")
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

tasks.dokkaHtmlMultiModule.configure {
    outputDirectory.set(layout.buildDirectory.dir("kdocs"))


    val dokkaBaseConfiguration = """
                {
                  "footerMessage": "Vapi4k"
                }
                """
    pluginsMapConfiguration.set(
        mapOf(
            // fully qualified plugin name to json configuration
            "org.jetbrains.dokka.base.DokkaBase" to dokkaBaseConfiguration
        )
    )
}
