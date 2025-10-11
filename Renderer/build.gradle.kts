plugins {
    id("java")
    application
}

group = "de.turboman.edit"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("org.slf4j:slf4j-api:2.0.13")
    implementation("ch.qos.logback:logback-classic:1.5.13")

    implementation("net.bramp.ffmpeg:ffmpeg:0.8.0")
    implementation("commons-cli:commons-cli:1.10.0")
    implementation("com.google.protobuf:protobuf-java:4.32.1")
    implementation("org.java-websocket:Java-WebSocket:1.6.0")

    implementation(project(":Shared"))
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("de.turboman.edit.renderer.Renderer")
}