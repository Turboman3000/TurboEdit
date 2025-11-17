package turboedit.editor.windows.errors

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import javafx.stage.Modality
import javafx.stage.Stage
import turboedit.editor.Editor
import turboedit.editor.misc.Locale

class FileErrorWindow(title: String?, text: String?, files: ArrayList<String?>) : Stage() {
    init {
        val fontName = Font.getDefault().name

        icons.add(Editor.Companion.ICON)
        isAlwaysOnTop = true
        isResizable = false
        width = 800.0;

        val label = Text(text)
        label.font = Font.font(fontName, FontWeight.MEDIUM, 14.0)

        val fileList = ListView<String?>()
        fileList.isEditable = false

        for (file in files) {
            fileList.getItems().add(file)
        }

        val closeButton = Button(Locale.getText("error.button.close"))
        closeButton.cursor = Cursor.HAND
        closeButton.onAction = EventHandler { event: ActionEvent? -> close() }

        val buttonBox = HBox(closeButton)
        buttonBox.spacing = 6.0
        buttonBox.alignment = Pos.CENTER_RIGHT

        val box = VBox(label, fileList, buttonBox)
        box.spacing = 6.0
        box.alignment = Pos.CENTER_LEFT
        box.padding = Insets(15.0)

        scene = Scene(box);

        setTitle(Editor.Companion.TITLE + " - " + title)
        centerOnScreen()
        initModality(Modality.APPLICATION_MODAL)
        showAndWait()
    }
}
