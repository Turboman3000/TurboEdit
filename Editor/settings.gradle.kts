plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "editor"

include(":shared")
project(":shared").projectDir = File("../Shared")