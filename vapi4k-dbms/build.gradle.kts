plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.dokka)
}

dependencies {
    api(project(":vapi4k-utils"))

    api(libs.micrometer.registry.prometheus)

    implementation(libs.hikari)
    implementation(libs.pgjdbc.ng)
    implementation(libs.postgres)

    api(libs.exposed.core)
    api(libs.exposed.jdbc)
    api(libs.exposed.json)
    api(libs.exposed.kotlin.datetime)

    testImplementation(kotlin("test"))
    testImplementation(libs.kluent)
    testImplementation(libs.ktor.server.tests)
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

dokka {
    pluginsConfiguration.html {
        homepageLink.set("https://github.com/vapi4k/vapi4k")
        footerMessage.set("vapi4k-dbms")
    }
    dokkaSourceSets.configureEach {
        sourceLink {
            includes.from("../docs/packages.md")
            localDirectory.set(file("src/main/kotlin"))
            remoteUrl.set(uri("https://github.com/vapi4k/vapi4k/blob/master/vapi4k-dbms/src/main/kotlin"))
            remoteLineSuffix.set("#L")
        }
    }
}
