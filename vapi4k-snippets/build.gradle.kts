plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
    `java-library`
}

dependencies {
    implementation(project(":vapi4k-core"))
}

kotlin {
    jvmToolchain(11)
}
