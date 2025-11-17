package turboedit.editor.windows

import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import javafx.scene.text.Text
import javafx.scene.text.TextAlignment
import javafx.stage.Modality
import javafx.stage.Stage
import turboedit.editor.Editor
import turboedit.editor.misc.Locale

class AboutWindow : Stage() {
    init {
        icons.add(Editor.Companion.ICON)
        title = Editor.Companion.TITLE + " - " + Locale.getText("title.about")
        isResizable = false
        isAlwaysOnTop = true

        val height = 100
        maxHeight = height.toDouble()
        setHeight(height.toDouble())

        val width = 190
        maxWidth = width.toDouble()
        setWidth(width.toDouble())

        scene = Scene(this.pane)

        centerOnScreen()
        initModality(Modality.APPLICATION_MODAL)
        showAndWait()
    }

    private val pane: GridPane
        get() {
            val panel = GridPane()

            panel.vgap = 5.0
            panel.hgap = 5.0
            panel.alignment = Pos.CENTER

            val text1 = Text(
                Locale.getText(
                    "menubar.help.about",
                    Editor.Companion.TITLE
                )
            )
            text1.textAlignment = TextAlignment.CENTER
            panel.add(text1, 0, 0, 1, 1)

            val text2 = Text("(c) 2025 TurboMedia")
            text2.textAlignment = TextAlignment.CENTER
            panel.add(text2, 0, 1, 1, 1)

            return panel
        }
}
