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

    implementation(libs.hikari)
    implementation(libs.pgjdbc.ng)
    implementation(libs.postgres)

    api(libs.exposed.core)
    api(libs.exposed.jdbc)
    api(libs.exposed.json)
    api(libs.exposed.kotlin.datetime)

    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.test)
    // testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

java {
    withSourcesJar()
}

kotlin {
    jvmToolchain(17)

    sourceSets.all {
        languageSettings.optIn("kotlinx.serialization.ExperimentalSerializationApi")
    }
}

dokka {
    pluginsConfiguration.html {
        footerMessage.set("vapi4k-dbms")
    }
    dokkaSourceSets.configureEach {
        sourceLink {
            localDirectory.set(file("src/main/kotlin"))
            remoteUrl.set(uri("https://github.com/vapi4k/vapi4k/blob/master/vapi4k-dbms/src/main/kotlin"))
            remoteLineSuffix.set("#L")
        }
    }
}

//kotlinter {
//    failBuildWhenCannotAutoFormat = false
//    ignoreFailures = true
//    reporters = arrayOf("checkstyle", "plain")
//}
