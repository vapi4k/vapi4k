pluginManagement {
  repositories {
//    maven(url = "./maven-repo")
    gradlePluginPortal()
  }
}

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "vapi4k"

include("vapi4k-core")
include("vapi4k-dbms")
include("vapi4k-utils")
include("vapi4k-snippets")
