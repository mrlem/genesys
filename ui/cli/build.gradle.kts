plugins {
    application
}

dependencies {
    implementation(project(":core"))
    implementation(libs.kotlinx.cli)

    testImplementation(kotlin("test"))
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.params)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.mockk)
}

application {
    mainClass.set("Launcher")
}
