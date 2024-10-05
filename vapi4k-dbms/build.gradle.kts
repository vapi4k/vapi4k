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
