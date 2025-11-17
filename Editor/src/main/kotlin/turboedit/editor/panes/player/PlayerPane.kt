package turboedit.editor.panes.player

import javafx.geometry.Insets
import javafx.scene.layout.VBox
import java.io.IOException

class PlayerPane : VBox() {
    init {
        spacing = 6.0
        minWidth = 1400.0
        minHeight = 800.0
        padding = Insets(15.0)

        try {
            children.add(PlayerPlayback())
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

        children.add(PlayerControls())
    }
}