package turboedit.editor.panes.timeline

import javafx.geometry.Insets
import javafx.scene.Cursor
import javafx.scene.control.Button
import javafx.scene.control.Tooltip
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

class LayerBox(type: LayerType) : VBox() {
    init {
        val fontName = Font.getDefault().name

        val buttonBox = HBox()
        val textBox = HBox()

        var icon = Text()
        var layerName = ""

        when (type) {
            LayerType.AUDIO -> {
                layerName = "Audio 1"
                icon = FontIcon.of(FluentUiFilledMZ.SPEAKER_24)
            }

            LayerType.VIDEO -> {
                layerName = "Video 1"
                icon = FontIcon.of(FluentUiFilledMZ.VIDEO_24)
            }
        }

        icon.translateY = 2.0

        val layerLabel = Text(layerName)

        textBox.children.add(icon)
        textBox.children.add(layerLabel)

        layerLabel.textAlignment = TextAlignment.LEFT
        layerLabel.font = Font.font(fontName, FontWeight.BLACK, 16.0)

        val lockButton = Button()
        lockButton.cursor = Cursor.HAND
        lockButton.graphic = FontIcon.of(FluentUiFilledMZ.UNLOCK_24)
        lockButton.tooltip = Tooltip(Locale.getText("timeline.layer.general.lock"))

        buttonBox.children.add(lockButton)

        if (type == LayerType.VIDEO) {
            val visibleButton = Button()
            visibleButton.cursor = Cursor.HAND
            visibleButton.graphic = FontIcon.of(FluentUiFilledAL.EYE_SHOW_24)
            visibleButton.tooltip = Tooltip(Locale.getText("timeline.layer.video.visible"))

            buttonBox.children.add(visibleButton)
        }

        if (type == LayerType.AUDIO) {
            val soloButton = Button()
            soloButton.cursor = Cursor.HAND
            soloButton.graphic = FontIcon.of(FluentUiFilledMZ.SPEAKER_NONE_24)
            soloButton.tooltip = Tooltip(Locale.getText("timeline.layer.audio.solo.disabled"))

            val muteButton = Button()
            muteButton.cursor = Cursor.HAND
            muteButton.graphic = FontIcon.of(FluentUiFilledMZ.SPEAKER_OFF_24)
            muteButton.tooltip = Tooltip(Locale.getText("timeline.layer.audio.mute.disabled"))

            val editButton = Button()
            editButton.cursor = Cursor.HAND
            editButton.graphic = FontIcon.of(FluentUiFilledAL.EDIT_24)
            editButton.tooltip = Tooltip(Locale.getText("timeline.layer.audio.edit"))

            buttonBox.children.add(editButton)
            buttonBox.children.add(soloButton)
            buttonBox.children.add(muteButton)
        }

        buttonBox.spacing = 6.0
        textBox.spacing = 6.0

        children.add(textBox)
        children.add(buttonBox)

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

        spacing = 8.0
        padding = Insets(8.0)
        minWidth = 280.0
        maxWidth = 280.0
    }

    enum class LayerType {
        VIDEO,
        AUDIO
    }
}
