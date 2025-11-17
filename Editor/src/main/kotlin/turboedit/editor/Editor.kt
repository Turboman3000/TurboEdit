package turboedit.editor

import javafx.application.Application
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority
import javafx.stage.Stage
import javafx.stage.WindowEvent
import net.harawata.appdirs.AppDirs
import net.harawata.appdirs.AppDirsFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import turboedit.editor.components.MenuBar
import turboedit.editor.events.EventSystem
import turboedit.editor.events.EventType
import turboedit.editor.events.ShortcutHandler
import turboedit.editor.events.WindowResizeEventData
import turboedit.editor.integration.DiscordIntegration
import turboedit.editor.misc.Locale
import turboedit.editor.misc.PreferencesFile
import turboedit.editor.misc.StyleManager
import turboedit.editor.panes.file_browser.FileBrowserPane
import turboedit.editor.panes.player.PlayerPane
import turboedit.editor.panes.timeline.TimelinePane
import turboedit.shared.project.Project
import java.io.IOException
import java.io.InputStream
import java.util.*
import kotlin.system.exitProcess

class Editor : Application() {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun start(stage: Stage) {
        ICON = Image(Objects.requireNonNull<InputStream?>(javaClass.getResourceAsStream("/icon.png")))

        try {
            PreferencesFile.read()
        } catch (e: IOException) {
            logger.error(e.message)
        } catch (e: InterruptedException) {
            logger.error(e.message)
        }

        run {
            val project = Project(
                null,
                "Untitled Project",
                ProjectManager.CURRENT_VERSION,
                ArrayList(),
                ArrayList()
            )
            EventSystem.registerListener(EventType.LOADED_PROJECT, { dat: Any? ->
                val data = (dat as Project)
                if (data.path == null) {
                    stage.title = TITLE + " - " + data.name
                } else {
                    stage.title = TITLE + " - " + data.name + " [" + data.path + "]"
                }
            })

            ProjectManager.CURRENT_PROJECT = project
            EventSystem.callEvent(EventType.LOADED_PROJECT, project)
        }

        StyleManager.updateStyle()

        try {
            Locale().loadLocales()
        } catch (e: IOException) {
            logger.error("{} = {}", e.javaClass.getName(), e.message)
        }

        val column = ColumnConstraints()
        column.hgrow = Priority.ALWAYS

        val grid = GridPane()
        grid.columnConstraints.add(column)

        grid.add(MenuBar(stage), 0, 0, 2, 1)

        try {
            grid.add(FileBrowserPane(), 0, 1, 1, 1)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

        grid.add(PlayerPane(), 1, 1, 1, 1)
        grid.add(TimelinePane(), 0, 2, 2, 1)

        val scene = Scene(grid)

        ShortcutHandler.registerGlobalShortcuts(scene)

        stage.minWidth = 640.0
        stage.minHeight = 480.0
        stage.maxHeight = Double.Companion.MAX_VALUE
        stage.maxWidth = Double.Companion.MAX_VALUE
        stage.scene = scene
        stage.icons.add(ICON)
        stage.isMaximized = true
        stage.onCloseRequest = EventHandler { event: WindowEvent? -> exitProcess(0) }

        stage.show()
        stage.requestFocus()

        stage.widthProperty()
            .addListener((ChangeListener { obs: ObservableValue<out Number?>?, oldValue: Number?, newValue: Number? ->
                val data = WindowResizeEventData(
                    newValue!!.toInt(),
                    stage.height.toInt(),
                    newValue.toDouble() / 1920,
                    stage.height / 1080
                )
                EventSystem.callEvent(EventType.WINDOW_RESIZE, data)
            }))

        stage.heightProperty()
            .addListener((ChangeListener { obs: ObservableValue<out Number?>?, oldValue: Number?, newValue: Number? ->
                val data = WindowResizeEventData(
                    stage.width.toInt(),
                    newValue!!.toInt(),
                    stage.width / 1920,
                    newValue.toDouble() / 1080
                )
                EventSystem.callEvent(EventType.WINDOW_RESIZE, data)
            }))

        val data = WindowResizeEventData(
            stage.width.toInt(),
            stage.height.toInt(),
            stage.width / 1920,
            stage.height / 1080
        )

        EventSystem.callEvent(EventType.WINDOW_RESIZE, data)
        DiscordIntegration().start()
    }

    fun startApp() {
        launch()
    }

    companion object {
        const val TITLE: String = "TurboEdit Alpha 1"

        val APP_DIRS: AppDirs = AppDirsFactory.getInstance()
        val APPDATA: String = APP_DIRS.getUserDataDir("TurboEdit", null, "TurboMedia", true)
        var ICON: Image? = null
    }
}
