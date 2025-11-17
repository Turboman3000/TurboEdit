plugins {
    id("java")
    id("com.google.protobuf") version "0.9.5"
    kotlin("jvm") version "2.2.21"
}

group = "turboedit"
version = "alpha-1.0"

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("com.google.protobuf:protobuf-java:4.32.1")
    implementation("org.msgpack:msgpack-core:0.9.10")
    implementation("net.bramp.ffmpeg:ffmpeg:0.8.0")

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

kotlin {
    jvmToolchain(21)
}