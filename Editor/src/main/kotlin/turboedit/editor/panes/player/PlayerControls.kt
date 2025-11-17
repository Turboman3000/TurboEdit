package turboedit.editor.panes.player

import atlantafx.base.controls.Spacer
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.control.Button
import javafx.scene.control.Slider
import javafx.scene.input.MouseEvent
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import org.kordamp.ikonli.fluentui.FluentUiFilledMZ
import org.kordamp.ikonli.javafx.FontIcon
import turboedit.editor.events.EventSystem.EventAction
import turboedit.editor.events.EventSystem.callEvent
import turboedit.editor.events.EventSystem.registerListener
import turboedit.editor.events.EventType
import turboedit.editor.events.ThemeChangedEventData
import turboedit.editor.misc.StyleManager
import turboedit.editor.misc.StyleManager.callEvent

class PlayerControls : VBox() {
    init {
        val fontName = Font.getDefault().name

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

        val slider = Slider()
        slider.cursor = Cursor.OPEN_HAND

        slider.onMousePressed = EventHandler { event: MouseEvent? -> slider.cursor = Cursor.CLOSED_HAND }
        slider.onMouseReleased = EventHandler { event: MouseEvent? -> slider.cursor = Cursor.OPEN_HAND }

        val timestampFont = Font.font(fontName, FontWeight.BOLD, 20.0)

        val currentTimestamp = Text("00:00:00")
        currentTimestamp.font = timestampFont

        val totalTimestamp = Text("00:42:00")
        totalTimestamp.font = timestampFont

        val previousButton = Button()
        previousButton.isFocusTraversable = false
        previousButton.graphic = FontIcon(FluentUiFilledMZ.PREVIOUS_24)
        previousButton.cursor = Cursor.HAND

        val playButton = Button()
        playButton.isFocusTraversable = false
        playButton.onAction = EventHandler { event: ActionEvent? -> callEvent(EventType.PLAYBACK_TOGGLE) }

        registerListener(EventType.PLAYBACK_TOGGLE, EventAction { data: Any? ->
            playbackState = !playbackState
            playButton.graphic = FontIcon(if (playbackState) FluentUiFilledMZ.PAUSE_24 else FluentUiFilledMZ.PLAY_24)
        })

        playButton.graphic = FontIcon(FluentUiFilledMZ.PLAY_24)
        playButton.cursor = Cursor.HAND

        val skipButton = Button()
        skipButton.isFocusTraversable = false
        skipButton.graphic = FontIcon(FluentUiFilledMZ.NEXT_24)
        skipButton.cursor = Cursor.HAND

        val hbox = HBox(currentTimestamp, Spacer(), previousButton, playButton, skipButton, Spacer(), totalTimestamp)

        hbox.alignment = Pos.CENTER
        hbox.spacing = 6.0

        padding = Insets(6.0)

        spacing = 6.0
        children.add(slider)
        children.add(hbox)
    }

    companion object {
        private var playbackState = false
    }
}
