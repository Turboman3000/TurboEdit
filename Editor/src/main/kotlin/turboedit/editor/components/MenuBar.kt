package turboedit.editor.components

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import javafx.stage.FileChooser
import javafx.stage.Stage
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import turboedit.editor.Editor
import turboedit.editor.ProjectManager.loadProject
import turboedit.editor.misc.Locale
import turboedit.editor.misc.Locale.Companion.getText
import turboedit.editor.windows.AboutWindow
import turboedit.editor.windows.NewProjectWindow
import turboedit.editor.windows.RenderQueueWindow
import turboedit.editor.windows.preferences.PreferencesWindow
import java.awt.Desktop
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException
import kotlin.system.exitProcess

class MenuBar(stage: Stage?) : MenuBar() {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    init {
        menus.add(getMenuFile(stage))
        menus.add(this.menuEdit)

        menus.add(this.menuHelp)
    }

    private fun getMenuFile(stage: Stage?): Menu {
        val menu = Menu(Locale.getText("menubar.file"))

        val itemNewProject = MenuItem(Locale.getText("menubar.file.new_project"))
        itemNewProject.onAction = EventHandler { event: ActionEvent? -> NewProjectWindow(stage) }
        menu.items.add(itemNewProject)

        val itemLoadProject = MenuItem(Locale.getText("menubar.file.load_project"))
        itemLoadProject.onAction = EventHandler { event: ActionEvent? ->
            val chooser = FileChooser()

            chooser.title = Editor.Companion.TITLE + " - " + Locale.getText("title.open_project")
            chooser.extensionFilters.addAll(
                FileChooser.ExtensionFilter(Locale.getText("file_chooser.ext.tvp"), "*.tvp")
            )

            val file = chooser.showOpenDialog(stage)

            if (file == null) {
                return@EventHandler
            }

            try {
                loadProject(file)
            } catch (e: IOException) {
                logger.error("{} = {}", e.javaClass.getName(), e.message)
            }
        }

        menu.items.add(itemLoadProject)

        val itemPreferences = MenuItem(Locale.getText("menubar.file.preferences"))
        itemPreferences.onAction = EventHandler { event: ActionEvent? -> PreferencesWindow() }
        menu.items.add(itemPreferences)

        val renderQueueMenu = MenuItem(Locale.getText("menubar.file.render_queue"))
        renderQueueMenu.onAction = EventHandler { event: ActionEvent? -> RenderQueueWindow() }
        menu.items.add(renderQueueMenu)

        val itemQuit = MenuItem(Locale.getText("menubar.file.quit"))
        itemQuit.onAction = EventHandler { event: ActionEvent? -> exitProcess(0) }

        menu.items.add(itemQuit)

        return menu
    }

    private val menuEdit: Menu
        get() {
            val menu =
                Menu(Locale.getText("menubar.edit"))

            return menu
        }

    private val menuHelp: Menu
        get() {
            val menu =
                Menu(Locale.getText("menubar.help"))

            val itemAbout = MenuItem(
                getText(
                    "menubar.help.about",
                    Editor.Companion.TITLE
                )
            )
            itemAbout.onAction = EventHandler { event: ActionEvent? -> AboutWindow() }
            menu.items.add(itemAbout)

            val itemGithub =
                MenuItem(Locale.getText("menubar.help.repo"))
            itemGithub.onAction = EventHandler { event: ActionEvent? ->
                if (Desktop.isDesktopSupported() && Desktop.getDesktop()
                        .isSupported(Desktop.Action.BROWSE)
                ) {
                    try {
                        Desktop.getDesktop().browse(URI("https://github.com/Turboman3000/TurboEdit"))
                    } catch (e: IOException) {
                        logger.error("{} = {}", e.javaClass.getName(), e.message)
                    } catch (e: URISyntaxException) {
                        logger.error("{} = {}", e.javaClass.getName(), e.message)
                    }
                }
            }
            menu.items.add(itemGithub)

            return menu
        }
}
