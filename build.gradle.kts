plugins {
    kotlin("jvm") version "1.8.20"
    application
}

group = "org.mrlem.genesys"
version = "1.0.2"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.familysearch.gedcom:gedcom:1.14.0") // FIXME - not fixed in the upsteam project, maybe submit a patch
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.5")
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
            from("LICENSE")
            into("screenshots") {
                from("screenshots")
            }
        }
    }
}