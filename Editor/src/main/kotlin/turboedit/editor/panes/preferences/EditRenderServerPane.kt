package turboedit.editor.panes.preferences

import atlantafx.base.controls.Spacer
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage
import turboedit.editor.events.EventSystem.callEvent
import turboedit.editor.events.EventType
import turboedit.editor.misc.Locale
import turboedit.editor.misc.PreferencesFile
import turboedit.editor.renderer.FileResolveMethod
import turboedit.editor.renderer.RenderServerEntry
import turboedit.editor.windows.SimpleMessageBox
import java.io.IOException
import java.util.*

class EditRenderServerPane(stage: Stage, entry: RenderServerEntry, responseID: String?) : VBox() {
    init {
        padding = Insets(15.0)
        spacing = 6.0

        val alreadyExists = PreferencesFile.CURRENT_PREFERENCES!!.renderServers.contains(entry)

        val serverNameLabel = Label(Locale.getText("edit_render_server.display_name"))
        val serverNameInput = TextField(entry.displayName())
        serverNameLabel.labelFor = serverNameInput
        serverNameInput.isDisable = entry.buildIn()

        val serverIPLabel = Label(Locale.getText("edit_render_server.ip"))
        val serverIPInput = TextField(entry.ip())
        serverIPLabel.labelFor = serverIPInput
        serverIPInput.isDisable = entry.buildIn()

        val fileModeLabel = Label(Locale.getText("edit_render_server.file_resolve"))
        val fileModeBox = ComboBox<FileResolveMethod?>()

        for (item in FileResolveMethod.entries) {
            fileModeBox.getItems().add(item)
        }

        fileModeBox.isDisable = entry.buildIn()
        fileModeBox.value = entry.fileResolveMethod()
        fileModeBox.maxWidth = Double.Companion.MAX_VALUE
        fileModeBox.cursor = Cursor.HAND
        fileModeLabel.labelFor = fileModeBox

        val defaultCheckbox = CheckBox(Locale.getText("edit_render_server.default_server"))
        defaultCheckbox.cursor = Cursor.HAND
        defaultCheckbox.isSelected = entry.defaultServer()

        val addButton = Button(Locale.getText("edit_render_server.button." + (if (alreadyExists) "edit" else "add")))
        addButton.cursor = Cursor.HAND
        addButton.onAction = EventHandler { event: ActionEvent? ->
            if (serverNameInput.text.isBlank()) {
                SimpleMessageBox(
                    Locale.getText("edit_render_server.error.no_display_name.title"),
                    Locale.getText("edit_render_server.error.no_display_name")
                )

                return@EventHandler
            }
            val newEntry = RenderServerEntry(
                UUID.randomUUID().toString(),
                serverNameInput.text,
                serverIPInput.text,
                fileModeBox.getValue(),
                defaultCheckbox.isSelected,
                entry.buildIn()
            )

            try {
                if (alreadyExists) {
                    PreferencesFile.CURRENT_PREFERENCES!!.removeRenderServers(entry)
                }

                PreferencesFile.CURRENT_PREFERENCES!!.addRenderServers(newEntry)
            } catch (e: IOException) {
                throw RuntimeException(e)
            } catch (e: InterruptedException) {
                throw RuntimeException(e)
            }

            callEvent(EventType.PREFERENCES_CHANGED, responseID)
            stage.close()
        }

        val closeButton = Button(Locale.getText("edit_render_server.button.close"))
        closeButton.cursor = Cursor.HAND
        closeButton.onAction = EventHandler { event: ActionEvent? -> stage.close() }

        val actionBox = HBox(defaultCheckbox, Spacer(), addButton, closeButton)
        actionBox.alignment = Pos.CENTER
        actionBox.spacing = 6.0

        children.addAll(
            serverNameLabel,
            serverNameInput,
            serverIPLabel,
            serverIPInput,
            fileModeLabel,
            fileModeBox,
            actionBox
        )
    }
}
