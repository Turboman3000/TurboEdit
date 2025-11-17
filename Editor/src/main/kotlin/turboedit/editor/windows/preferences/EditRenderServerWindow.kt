package turboedit.editor.windows.preferences

import javafx.scene.Scene
import javafx.stage.Modality
import javafx.stage.Stage
import turboedit.editor.Editor
import turboedit.editor.misc.Locale
import turboedit.editor.panes.preferences.EditRenderServerPane
import turboedit.editor.renderer.RenderServerEntry

class EditRenderServerWindow(entry: RenderServerEntry, responseID: String?) : Stage() {
    init {
        title = Editor.Companion.TITLE + " - " + Locale.getText("title.edit_render_server")
        icons.add(Editor.Companion.ICON)
        width = 500.0;
        isResizable = false
        scene = Scene(EditRenderServerPane(this, entry, responseID));

        centerOnScreen()
        initModality(Modality.APPLICATION_MODAL)
        showAndWait()
    }
}
