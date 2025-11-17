package turboedit.editor.panes.timeline

import javafx.geometry.Insets
import javafx.scene.Cursor
import javafx.scene.control.MenuButton
import javafx.scene.control.MenuItem
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import javafx.scene.text.TextAlignment
import org.kordamp.ikonli.fluentui.FluentUiFilledAL
import org.kordamp.ikonli.fluentui.FluentUiFilledMZ
import org.kordamp.ikonli.javafx.FontIcon
import turboedit.editor.events.EventSystem.EventAction
import turboedit.editor.events.EventSystem.registerListener
import turboedit.editor.events.EventType
import turboedit.editor.events.ThemeChangedEventData
import turboedit.editor.misc.Locale
import turboedit.editor.misc.StyleManager
import turboedit.editor.misc.StyleManager.callEvent

class UpperBar : HBox() {
    init {
        val fontName = Font.getDefault().name
        val timestamp = Text("00:00:00.0000")

        timestamp.font = Font.font(fontName, FontWeight.BLACK, 24.0)
        timestamp.textAlignment = TextAlignment.CENTER

        val newButton = MenuButton()
        newButton.cursor = Cursor.HAND
        newButton.graphic = FontIcon.of(FluentUiFilledAL.ADD_SQUARE_24)

        val audioItem = MenuItem(Locale.getText("timeline.upper_bar.add.audio"))
        audioItem.graphic = FontIcon.of(FluentUiFilledMZ.SPEAKER_24)
        newButton.items.add(audioItem)


        val settingsButton = MenuButton()
        settingsButton.cursor = Cursor.HAND
        settingsButton.graphic = FontIcon.of(FluentUiFilledMZ.SETTINGS_24)

        spacing = 8.0
        padding = Insets(8.0)
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
        })
        callEvent()

        children.add(timestamp)
        children.add(newButton)
        children.add(settingsButton)
    }
}
