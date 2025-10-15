rootProject.name = "editor"

include(":shared", ":renderer")
project(":shared").projectDir = File("../Shared")
project(":renderer").projectDir = File("../Renderer")