package turboedit.editor.windows

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import javafx.stage.Modality
import javafx.stage.Stage
import turboedit.editor.Editor

class SimpleMessageBox(title: String?, message: String?) : Stage() {
    init {
        val messageText = Text(message)
        val okButton = Button("Okay")

        okButton.cursor = Cursor.HAND
        okButton.onAction = EventHandler { event: ActionEvent? -> close() }

        val box = VBox(messageText, okButton)

        box.alignment = Pos.CENTER_RIGHT
        box.spacing = 6.0
        box.padding = Insets(15.0)

        isResizable = false
        scene = Scene(box)
        icons.add(Editor.Companion.ICON)
        isAlwaysOnTop = true

        initModality(Modality.APPLICATION_MODAL)
        setTitle(Editor.Companion.TITLE + " - " + title)
        centerOnScreen()
        show()
    }
}
