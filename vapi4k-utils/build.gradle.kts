plugins {
    // alias(libs.plugins.serialization)
    alias(libs.plugins.dokka)
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

java {
    withSourcesJar()
}

tasks.withType<Zip> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

dependencies {
    implementation(libs.ktor.server.core)

    api(libs.kotlin.serialization)
    api(libs.utils.json)

    api(libs.kotlin.logging)
    api(libs.logback.classic)

    testImplementation(libs.kluent)
    testImplementation(kotlin("test"))
}

dokka {
    pluginsConfiguration.html {
        homepageLink.set("https://github.com/vapi4k/vapi4k")
        footerMessage.set("vapi4k-utils")
    }
    dokkaSourceSets.configureEach {
        sourceLink {
            includes.from("../docs/packages.md")
            localDirectory.set(file("src/main/kotlin"))
            remoteUrl.set(uri("https://github.com/vapi4k/vapi4k/blob/master/vapi4k-utils/src/main/kotlin"))
            remoteLineSuffix.set("#L")
        }
    }
}
