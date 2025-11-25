plugins {
    kotlin("jvm") version "2.2.21"
    application
}

group = "org.mrlem.genesys"
version = "1.0.10"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.params)
    testRuntimeOnly(libs.junit.jupiter.engine)

    implementation(libs.familysearch.gedcom)
    implementation(libs.kotlinx.cli)
    implementation(libs.slf4j.nop)
}

tasks {
    test {
        useJUnitPlatform()
    }
    distTar {
        compression = Compression.GZIP
        archiveExtension.set("tar.gz")
    }
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("Main")
}

distributions {
    main {
        contents {
            from("README.md")
            from("LICENSE")
            into("screenshots") {
                from("screenshots")
            }
            into("sample") {
                from("sample")
            }
        }
    }
}