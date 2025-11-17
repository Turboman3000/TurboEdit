package turboedit.editor.panes.timeline

import javafx.geometry.Insets
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import turboedit.editor.events.EventSystem.EventAction
import turboedit.editor.events.EventSystem.registerListener
import turboedit.editor.events.EventType
import turboedit.editor.events.ThemeChangedEventData
import turboedit.editor.misc.StyleManager
import turboedit.editor.misc.StyleManager.callEvent
import java.io.IOException

class ClipContainer(type: LayerBox.LayerType) : HBox() {
    init {
        spacing = 4.0
        maxWidth = Double.Companion.MAX_VALUE
        setHgrow(this, Priority.ALWAYS)

        registerListener(EventType.THEME_CHANGED, EventAction { dat: Any? ->
            val data = dat as ThemeChangedEventData?
            val color = when (data!!.theme) {
                StyleManager.Theme.DARK -> Color.color(1.0, 1.0, 1.0, 0.05)
                StyleManager.Theme.LIGHT -> Color.color(0.0, 0.0, 0.0, 0.05)
                else -> {}
            }

            val background = Background(BackgroundFill(color as Paint?, CornerRadii(10.0), Insets.EMPTY))

            setBackground(background)
            children.clear()

            try {
                children.add(ClipCard(type))
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        })

        callEvent()
    }
}
