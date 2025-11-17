package turboedit.editor.panes.player

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.CacheHint
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.GridPane
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.shape.Rectangle
import turboedit.editor.events.EventSystem.EventAction
import turboedit.editor.events.EventSystem.registerListener
import turboedit.editor.events.EventType
import turboedit.editor.events.ThemeChangedEventData
import turboedit.editor.events.WindowResizeEventData
import turboedit.editor.misc.StyleManager
import turboedit.editor.misc.StyleManager.callEvent

class PlayerPlayback : GridPane() {
    init {
        val borderRadius = 10.0
        val padding = 12.0

        val resource = javaClass.getResource("/test2.png")
        val stream = resource!!.openStream()
        val previewImage = Image(stream)
        val view = ImageView(previewImage)

        view.isPreserveRatio = true
        view.isCache = true
        view.cacheHint = CacheHint.SPEED

        val clip = Rectangle(view.fitWidth, view.fitHeight)

        clip.arcWidth = borderRadius * 2
        clip.arcHeight = borderRadius * 2

        registerListener(EventType.WINDOW_RESIZE, EventAction { dat: Any? ->
            val data = dat as WindowResizeEventData?
            view.fitHeight = 555 * data!!.heightScale - (padding * 2)

            clip.width = view.layoutBoundsProperty().get().width
            clip.height = view.layoutBoundsProperty().get().height
        })

        view.clip = clip
        registerListener(EventType.THEME_CHANGED, EventAction { dat: Any? ->
            val data = dat as ThemeChangedEventData?
            val color = when (data!!.theme) {
                StyleManager.Theme.DARK -> Color.color(1.0, 1.0, 1.0, 0.05)
                StyleManager.Theme.LIGHT -> Color.color(0.0, 0.0, 0.0, 0.05)
                else -> {}
            }

            val background = Background(BackgroundFill(color as Paint?, CornerRadii(borderRadius), Insets.EMPTY))
            setBackground(background)
        })

        callEvent()

        alignment = Pos.CENTER
        setPadding(Insets(padding))
        add(view, 0, 0, 1, 1)
    }
}
