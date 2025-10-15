plugins {
    id("java")
    application
    id("org.openjfx.javafxplugin") version "0.0.14"
}

group = "org.turbomedia.turboedit"
version = "alpha-1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("org.slf4j:slf4j-api:2.0.13")
    implementation("ch.qos.logback:logback-classic:1.5.13")

    implementation("io.github.mkpaz:atlantafx-base:2.1.0")
    implementation("org.kordamp.ikonli:ikonli-javafx:12.4.0")
    implementation("org.kordamp.ikonli:ikonli-fluentui-pack:12.4.0")

    implementation("org.msgpack:msgpack-core:0.9.10")
    implementation("org.bytedeco:javacv-platform:1.5.12")
    implementation("com.google.protobuf:protobuf-java:4.32.1")
    implementation("org.java-websocket:Java-WebSocket:1.6.0")
    implementation("commons-cli:commons-cli:1.10.0")
    implementation("net.harawata:appdirs:1.4.0")
    implementation("com.google.code.gson:gson:2.13.2")

    implementation(project(":shared"))
    implementation(project(":renderer"))
}

javafx {
    version = "21"
    modules = listOf("javafx.controls")
}

application {
    applicationName = "TurboEdit"
    mainClass = "org.turbomedia.turboedit.editor.Editor"
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "org.turbomedia.turboedit.editor.Editor"
        attributes["Class-Path"] = configurations.runtimeClasspath.get()
            .joinToString(separator = " ") { dependencyFile ->
                "lib/${dependencyFile.name}"
            }
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("--enable-preview")
}

var commonDistribution: CopySpec = copySpec {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(tasks.jar) {
        into(".")
        include(
            "editor*"
        )
    }
    from(configurations.runtimeClasspath) {
        into("lib")
        exclude(
            "editor*"
        )
    }
    exclude(
        "*-android-*",
        "*ios*",
        "*armhf*",
        "*x86.*",
        "*riscv*",
        "*ppc*"
    )
    include(
        "appdirs*",
        "atlantafx*",
        "commons*",
        "ffmpeg*",
        "gson*",
        "guava*",
        "ikonli*",
        "*WebSocket*",
        "javacpp*",
        "javafx*",
        "javacv*",
        "jna*",
        "jsr*",
        "logback*",
        "msgpack*",
        "protobuf*",
        "shared*",
        "renderer*",
        "slf4j*",
        "failureaccess*",
        "error_prone_*",
        "checker*"
    )
}

val osTargets = mapOf(
    "windows" to mapOf("GOOS" to "windows", "GOARCH" to "amd64", "EXT" to ".exe"),
    "linux" to mapOf("GOOS" to "linux", "GOARCH" to "amd64", "EXT" to ""),
    "macos" to mapOf("GOOS" to "darwin", "GOARCH" to "arm64", "EXT" to ".app")
)
val goScriptName = "../Binary/main.go"
val executableName = "TurboEdit"

val compileTasks = osTargets.map { (osName, vars) ->
    val goOs = vars.getValue("GOOS")
    val goArch = vars.getValue("GOARCH")
    val ext = vars.getValue("EXT")
    val taskName = "compileGoFor${osName.capitalize()}"
    val outputDir = layout.buildDirectory.dir("executables/$osName/")
    val outputFile = "${outputDir.get()}/$executableName$ext"

    tasks.register<Exec>(taskName) {
        group = "build"
        description = "Compiles Go script for $osName/$goArch"
        outputs.upToDateWhen { false }
        outputs.dirs(outputDir)

        commandLine("go", "generate")
        commandLine("go", "build", "-o", outputFile, "-ldflags", "-s -w -H=windowsgui", goScriptName)

        environment("GOOS", goOs)
        environment("GOARCH", goArch)

        outputs.file(outputFile)
    }
}.toSet()

distributions {
    configureEach {
        val distName = name
        val config = osTargets[distName] ?: return@configureEach

        val osName = config.getValue("GOOS")
        val ext = config.getValue("EXT")
        val compileTaskName = "compileGoFor${distName.capitalize()}"
        val compiledFilePath =
            layout.buildDirectory.file("executables/$distName/$executableName$ext").get().asFile.absolutePath

        contents {
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
            from(tasks.named(compileTaskName))
            from(compiledFilePath) {
                into(".")
                if (osName != "windows") {
                    fileMode = 0x755
                }
            }
        }
    }

    create("windows") {
        distributionBaseName = "TurboEdit"
        distributionClassifier = "windows"
        contents {
            with(commonDistribution)
            exclude(
                "*macosx*",
                "*linux*",
                "arm64"
            )
        }
    }
    create("linux") {
        distributionBaseName = "TurboEdit"
        distributionClassifier = "linux"
        contents {
            with(commonDistribution)
            exclude(
                "*macosx*",
                "*win*",
                "*windows*",
                "arm64"
            )
        }
    }
    create("macos") {
        distributionBaseName = "TurboEdit"
        distributionClassifier = "macos"
        contents {
            with(commonDistribution)
            exclude(
                "*linux*",
                "*win*",
                "*windows*",
                "*x64*",
                "*x86*"
            )
        }
    }
}

tasks.register("compileAllGo") {
    group = "build"
    description = "Compiles the Go script for all defined OS targets."
    dependsOn(compileTasks)
}

tasks.register("assembleAllZips") {
    group = "distribution"

    dependsOn(
        tasks.matching { it.name.endsWith("DistZip") }
    )
}