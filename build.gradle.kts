plugins {
    kotlin("jvm") version "1.8.20"
    application
}

group = "org.mrlem.genesys"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.familysearch.gedcom:gedcom:1.14.0")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("Main")
}

distributions {
    main {
        contents {
            from("README.md")
            into("screenshots") {
                from("screenshots")
            }
        }
    }
}