import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
    alias(libs.plugins.config)
    alias(libs.plugins.dokka)
    `java-library`
}

val versionStr: String by extra
val releaseDate: String by extra

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = group.toString()
            artifactId = project.name
            version = versionStr
            from(components["java"])
        }
    }
}

buildConfig {
    useKotlinOutput()
    packageName(project.group.toString())
    documentation.set("Generated by BuildConfig plugin")
    buildConfigField("String", "APP_NAME", "\"${project.name}\"")
    buildConfigField("String", "VERSION", provider { "\"${project.version}\"" })
    buildConfigField("String", "RELEASE_DATE", "\"$releaseDate\"")
    buildConfigField("long", "BUILD_TIME", "${System.currentTimeMillis()}L")
}

dependencies {
    api(project(":vapi4k-utils"))

    api(libs.ktor.client.core)
    api(libs.ktor.client.cio)
    api(libs.ktor.client.websockets)
    api(libs.ktor.client.encoding)
    api(libs.ktor.client.content.negotiation)

    api(libs.ktor.server.core)
    api(libs.ktor.server.cio)
    api(libs.ktor.server.websockets)
    api(libs.ktor.server.compression)
    api(libs.ktor.server.auth)
    api(libs.ktor.server.content.negotiation)
    api(libs.ktor.server.call.logging)
    api(libs.ktor.server.html.builder)
    api(libs.ktor.server.metrics.micrometer)

    api(libs.ktor.serialization)

    api(libs.micrometer.registry.prometheus)

    api(libs.exposed.kotlin.datetime)

    // testImplementation(libs.ktor.client.mock)
    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.test)
    // testImplementation(kotlin("test"))
}

sourceSets {
    named("main") {
        java.srcDir("build/generated/sources/buildConfig/main")
    }
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

tasks.withType<Zip> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks {
    register<Jar>("uberJar") {
        archiveClassifier.set("uber")
        from(sourceSets.main.get().output)
        dependsOn(configurations.runtimeClasspath)
        from({
            configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
        })
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    withType<DependencyUpdatesTask> {
        rejectVersionIf {
            listOf("BETA", "-RC").any { candidate.version.uppercase().contains(it) }
        }
    }
}

dokka {
    dokkaPublications.html {
        outputDirectory.set(layout.buildDirectory.dir("kdocs"))
    }
    pluginsConfiguration.html {
        footerMessage.set("vapi4k-core")
    }
    dokkaSourceSets.configureEach {
        sourceLink {
            localDirectory.set(file("src/main/kotlin"))
            remoteUrl.set(uri("https://github.com/vapi4k/vapi4k/blob/master/vapi4k-core/src/main/kotlin"))
            remoteLineSuffix.set("#L")
        }
    }
}

//kotlinter {
//    failBuildWhenCannotAutoFormat = false
//    ignoreFailures = true
//    reporters = arrayOf("checkstyle", "plain")
//}
