plugins {
    id("java")
    application
    id("org.openjfx.javafxplugin") version "0.0.14"
}

group = "de.turboman.edit"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("io.github.mkpaz:atlantafx-base:2.1.0")
    implementation("org.msgpack:msgpack-core:0.9.10")
    implementation("org.bytedeco:javacv-platform:1.5.12")
    implementation("com.google.protobuf:protobuf-java:4.32.1")
    implementation("org.java-websocket:Java-WebSocket:1.6.0")
    implementation("net.harawata:appdirs:1.4.0")

    implementation(project(":Shared"))
}

tasks.test {
    useJUnitPlatform()
}

javafx {
    version = "21"
    modules("javafx.controls")
}

application {
    mainClass.set("de.turboman.edit.editor.Editor")
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("--enable-preview")
}