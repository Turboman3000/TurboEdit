package turboedit.editor.panes.preferences

import atlantafx.base.controls.Spacer
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.control.ListView
import javafx.scene.control.Tab
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import org.kordamp.ikonli.fluentui.FluentUiFilledAL
import org.kordamp.ikonli.javafx.FontIcon
import turboedit.editor.events.EventSystem.EventAction
import turboedit.editor.events.EventSystem.registerListener
import turboedit.editor.events.EventType
import turboedit.editor.misc.Locale
import turboedit.editor.misc.PreferencesFile
import turboedit.editor.renderer.RenderServerEntry
import turboedit.editor.windows.preferences.AddRenderServerWindow
import turboedit.editor.windows.preferences.EditRenderServerWindow
import java.io.IOException
import java.util.*

class RenderingTab : Tab() {
    private val responseID: String? = UUID.randomUUID().toString()

    init {
        val fontName = Font.getDefault().name

        val box = VBox()
        box.padding = Insets(15.0)
        box.spacing = 10.0

        run {
            val headerText = Text(Locale.getText("preferences.rendering.title.render_servers"))
            headerText.font = Font.font(fontName, FontWeight.BOLD, 14.0)

            val serverList = ListView<RenderServerEntry>()
            serverList.getItems().addAll(PreferencesFile.CURRENT_PREFERENCES!!.renderServers)

            val editButton = Button(Locale.getText("preferences.rendering.button.edit"))
            editButton.cursor = Cursor.HAND
            editButton.isDisable = true
            editButton.graphic = FontIcon.of(FluentUiFilledAL.EDIT_24)

            editButton.onAction = EventHandler { event: ActionEvent ->
                val selected: RenderServerEntry = serverList.getSelectionModel().getSelectedItems()[0]
                EditRenderServerWindow(selected, responseID)
            }

            val newButton = Button(Locale.getText("preferences.rendering.button.add"))
            newButton.cursor = Cursor.HAND
            newButton.graphic = FontIcon.of(FluentUiFilledAL.ADD_24)
            newButton.onAction = EventHandler { event: ActionEvent? -> AddRenderServerWindow(responseID) }

            val deleteButton = Button(Locale.getText("preferences.rendering.button.delete"))
            deleteButton.cursor = Cursor.HAND
            deleteButton.isDisable = true
            deleteButton.graphic = FontIcon.of(FluentUiFilledAL.DELETE_24)

            serverList.onMouseClicked = EventHandler { event: MouseEvent? ->
                val items = serverList.getSelectionModel().getSelectedItems()

                if (items.isEmpty()) {
                    return@EventHandler
                }

                val selected: RenderServerEntry = items[0]

                editButton.isDisable = false
                deleteButton.isDisable = selected.buildIn()
            }

            deleteButton.onAction = EventHandler { e: ActionEvent? ->
                var selected: RenderServerEntry = serverList.getSelectionModel().getSelectedItems()[0]
                val size = serverList.getItems().size

                for (x in 0..<size) {
                    val item = serverList.getItems()[x]

                    if (selected == item) {
                        serverList.getItems().removeAt(x)

                        try {
                            PreferencesFile.CURRENT_PREFERENCES!!.removeRenderServers(selected)
                        } catch (ex: IOException) {
                            throw RuntimeException(ex)
                        } catch (ex: InterruptedException) {
                            throw RuntimeException(ex)
                        }
                        break
                    }
                }

                serverList.refresh()

                selected = serverList.getSelectionModel().getSelectedItems()[0]
                deleteButton.isDisable = selected.buildIn()
            }

            registerListener(EventType.PREFERENCES_CHANGED, EventAction { dat: Any? ->
                if (dat !== responseID) {
                    return@EventAction
                }

                serverList.getItems().clear()
                serverList.getItems().addAll(PreferencesFile.CURRENT_PREFERENCES!!.renderServers)
            })

            val showIPsCheckbox = CheckBox(Locale.getText("preferences.rendering.button.show_ips"))
            showIPsCheckbox.cursor = Cursor.HAND
            showIPsCheckbox.isSelected = PreferencesFile.CURRENT_PREFERENCES!!.showIPsForServers
            showIPsCheckbox.onAction = EventHandler { event: ActionEvent? ->
                try {
                    PreferencesFile.CURRENT_PREFERENCES!!.showIPsForServers(showIPsCheckbox.isSelected)
                } catch (e: IOException) {
                    throw RuntimeException(e)
                } catch (e: InterruptedException) {
                    throw RuntimeException(e)
                }
                serverList.refresh()
            }

            val actions = HBox(showIPsCheckbox, Spacer(), deleteButton, editButton, newButton)
            actions.spacing = 6.0
            actions.alignment = Pos.CENTER

            box.children.add(headerText)
            box.children.add(serverList)
            box.children.add(actions)
        }

        text = Locale.getText("preferences.rendering.tab")
        content = box
    }
}
