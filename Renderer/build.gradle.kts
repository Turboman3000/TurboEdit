plugins {
    id("java")
    application
}

group = "de.turboman.edit"
version = "alpha-1.0"

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

    implementation(project(":shared"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "org.turbomedia.turboedit.renderer.Renderer"
        attributes["Class-Path"] = configurations.runtimeClasspath.get()
            .joinToString(separator = " ") { dependencyFile ->
                "lib/${dependencyFile.name}"
            }

    }
}