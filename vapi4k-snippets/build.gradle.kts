plugins {
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(project(":vapi4k-core"))
}
