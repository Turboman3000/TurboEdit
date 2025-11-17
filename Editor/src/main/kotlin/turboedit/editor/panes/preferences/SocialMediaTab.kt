package turboedit.editor.panes.preferences

import javafx.geometry.Insets
import javafx.scene.control.Tab
import javafx.scene.layout.VBox
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import turboedit.editor.misc.Locale

class SocialMediaTab : Tab() {
    private val logger: Logger? = LoggerFactory.getLogger(javaClass)

    init {
        val box = VBox()
        box.padding = Insets(10.0)
        box.spacing = 10.0

        text = Locale.getText("preferences.social_media.tab")
        content = box
    }
}
