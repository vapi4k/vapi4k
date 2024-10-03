import org.jetbrains.dokka.DokkaConfiguration.Visibility

plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
    alias(libs.plugins.dokka)
    `java-library`
}

val versionStr: String by extra
val releaseDate: String by extra

description = project.name

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = group.toString()
            artifactId = description
            version = versionStr
            from(components["java"])
        }
    }
}

dependencies {
    api(project(":vapi4k-utils"))

    api(libs.micrometer.registry.prometheus)

    api(libs.hikari)
    api(libs.pgjdbc.ng)
    api(libs.postgres)
    api(libs.exposed.core)
    api(libs.exposed.jdbc)
    api(libs.exposed.json)
    api(libs.exposed.kotlin.datetime)

    api(libs.kotlin.logging)
    api(libs.logback.classic)

    testImplementation(libs.ktor.server.tests)
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

java {
    withSourcesJar()
}

kotlin {
    jvmToolchain(11)

    sourceSets.all {
        languageSettings.optIn("kotlinx.serialization.ExperimentalSerializationApi")
    }
}

kotlinter {
    failBuildWhenCannotAutoFormat = false
    ignoreFailures = true
    reporters = arrayOf("checkstyle", "plain")
}

tasks.dokkaHtml.configure {
//      "customAssets": ["${file("assets/my-image.png")}"],
//      "customStyleSheets": ["${file("assets/my-styles.css")}"],
//      "separateInheritedMembers": false,
//      "templatesDir": "${file("dokka/templates")}",
//      "mergeImplicitExpectActualDeclarations": false
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

    //    outputDirectory.set(buildDir.resolve("dokka"))
    dokkaSourceSets {
        named("main") {
            //displayName.set("Vapi4k")
            noStdlibLink.set(true)
            noJdkLink.set(true)

            documentedVisibilities.set(setOf(Visibility.PUBLIC))

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
