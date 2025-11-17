package turboedit.editor.windows.preferences

import javafx.scene.Scene
import javafx.stage.Modality
import javafx.stage.Stage
import turboedit.editor.Editor
import turboedit.editor.misc.Locale
import turboedit.editor.panes.preferences.EditRenderServerPane
import turboedit.editor.renderer.FileResolveMethod
import turboedit.editor.renderer.RenderServerEntry

class AddRenderServerWindow(responseID: String?) : Stage() {
    init {
        title = Editor.Companion.TITLE + " - " + Locale.getText("title.add_render_server")
        icons.add(Editor.Companion.ICON)
        isResizable = false
        scene = Scene(
            EditRenderServerPane(
                this, RenderServerEntry("", "", "", FileResolveMethod.STREAMING, false, false), responseID
            )
        );
        width = 500.0

        centerOnScreen()
        initModality(Modality.APPLICATION_MODAL)
        showAndWait()
    }
}
