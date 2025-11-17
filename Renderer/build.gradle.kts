plugins {
    id("java")
    kotlin("jvm") version "2.2.21"
    application
}

group = "turboedit"
version = "alpha-1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("org.slf4j:slf4j-api:2.0.13")
    implementation("ch.qos.logback:logback-classic:1.5.13")

    implementation("net.bramp.ffmpeg:ffmpeg:0.8.0")
    implementation("commons-cli:commons-cli:1.10.0")
    implementation("com.google.protobuf:protobuf-java:4.32.1")
    implementation("org.java-websocket:Java-WebSocket:1.6.0")

    implementation(project(":shared"))
    implementation(kotlin("stdlib-jdk8"))
}

sourceSets.main {
    java.srcDirs("src/main/java", "src/main/kotlin")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
    jvmTargetValidationMode.set(org.jetbrains.kotlin.gradle.dsl.jvm.JvmTargetValidationMode.WARNING)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

application {
    applicationName = "TurboEdit Renderer"
    mainClass = "turboedit.renderer.MainKt"
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "turboedit.renderer.MainKt"
        attributes["Class-Path"] = configurations.runtimeClasspath.get()
            .joinToString(separator = " ") { dependencyFile ->
                "lib/${dependencyFile.name}"
            }
    }
}