plugins {
    id("java")
    id("com.google.protobuf") version "0.9.5"
}

group = "de.turboman.edit"
version = "alpha-1.0"

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("com.google.protobuf:protobuf-java:4.32.1")
    implementation("org.msgpack:msgpack-core:0.9.10")
    implementation("net.bramp.ffmpeg:ffmpeg:0.8.0")
}

tasks.test {
    useJUnitPlatform()
}