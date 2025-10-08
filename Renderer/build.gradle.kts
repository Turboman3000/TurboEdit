plugins {
    id("java")
}

group = "de.turboman.edit"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation(project(":Shared"))
}

tasks.test {
    useJUnitPlatform()
}