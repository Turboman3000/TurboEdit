package turboedit.editor

import turboedit.editor.events.EventSystem
import turboedit.editor.events.EventType
import turboedit.shared.project.Project
import turboedit.shared.project.ProjectReader
import java.io.File
import java.io.IOException

object ProjectManager {
    @JvmField
    var CURRENT_PROJECT: Project? = null
    const val EXTENSION: String = ".tvp"
    const val CURRENT_VERSION: Int = 0

    @JvmStatic
    @Throws(IOException::class)
    fun loadProject(path: File) {
        CURRENT_PROJECT = ProjectReader.read(path.absolutePath)

        EventSystem.callEvent(EventType.LOADED_PROJECT, CURRENT_PROJECT)
        EventSystem.callEvent(EventType.DISCORD_UPDATE_ACTIVITY)
    }
}
