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

    implementation("org.msgpack:msgpack-core:0.9.10")
}

tasks.test {
    useJUnitPlatform()
}