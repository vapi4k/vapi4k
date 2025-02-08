pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

rootProject.name = "vapi4k"

include(
    "vapi4k-core",
    "vapi4k-dbms",
    "vapi4k-utils",
    "vapi4k-snippets",
)
