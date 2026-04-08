pluginManagement {
  repositories {
    mavenCentral()
    gradlePluginPortal()
  }
}

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention").version("1.0.0")
}

rootProject.name = "vapi4k"

include(":vapi4k-core")
include(":vapi4k-dbms")
include(":vapi4k-utils")
include(":vapi4k-snippets")
