package turboedit.editor.windows

import javafx.scene.Scene
import javafx.stage.Stage
import turboedit.editor.Editor
import turboedit.editor.misc.Locale
import turboedit.editor.panes.render_queue.RenderQueuePane

class RenderQueueWindow : Stage() {
    init {
        title = Editor.Companion.TITLE + " - " + Locale.getText("title.render_queue")
        icons.add(Editor.Companion.ICON)
        width = 500.0
        height = 250.0
        scene = Scene(RenderQueuePane())
        isAlwaysOnTop = true

        centerOnScreen()
        showAndWait()
    }
}
