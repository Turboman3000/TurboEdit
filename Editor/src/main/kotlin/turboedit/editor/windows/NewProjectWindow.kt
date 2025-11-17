package turboedit.editor.windows

import javafx.scene.Scene
import javafx.stage.Modality
import javafx.stage.Stage
import turboedit.editor.Editor
import turboedit.editor.misc.Locale
import turboedit.editor.panes.NewProjectPane

class NewProjectWindow(stage: Stage?) : Stage() {
    init {
        icons.add(Editor.Companion.ICON)
        title = Editor.Companion.TITLE + " - " + Locale.getText("title.new_project")
        isResizable = false
        isAlwaysOnTop = true

        val height = 250
        maxHeight = height.toDouble()
        setHeight(height.toDouble())

        val width = 450
        maxWidth = width.toDouble()
        setWidth(width.toDouble())

        scene = Scene(NewProjectPane(this, stage))

        centerOnScreen()
        initModality(Modality.APPLICATION_MODAL)
        showAndWait()
    }
}
