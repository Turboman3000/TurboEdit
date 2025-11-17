package turboedit.editor.windows.preferences

import javafx.scene.Scene
import javafx.stage.Modality
import javafx.stage.Stage
import turboedit.editor.Editor
import turboedit.editor.misc.Locale
import turboedit.editor.panes.preferences.PreferencesPane

class PreferencesWindow : Stage() {
    init {
        title = Editor.Companion.TITLE + " - " + Locale.getText("title.preferences")
        icons.add(Editor.Companion.ICON)

        val width = 600
        minWidth = width.toDouble()
        setWidth(width.toDouble())

        val height = 700
        minHeight = height.toDouble()
        setHeight(height.toDouble())

        scene = Scene(PreferencesPane())

        centerOnScreen()
        initModality(Modality.APPLICATION_MODAL)
        showAndWait()
    }
}
