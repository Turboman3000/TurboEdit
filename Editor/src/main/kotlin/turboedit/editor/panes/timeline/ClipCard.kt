package turboedit.editor.panes.timeline

import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.CacheHint
import javafx.scene.Cursor
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import javafx.scene.control.SeparatorMenuItem
import javafx.scene.effect.ColorAdjust
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.ContextMenuEvent
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.text.Text
import org.kordamp.ikonli.fluentui.FluentUiFilledAL
import org.kordamp.ikonli.fluentui.FluentUiFilledMZ
import org.kordamp.ikonli.javafx.FontIcon
import turboedit.editor.events.ClipSelectedEventData
import turboedit.editor.events.EventSystem.EventAction
import turboedit.editor.events.EventSystem.callEvent
import turboedit.editor.events.EventSystem.registerListener
import turboedit.editor.events.EventType
import turboedit.editor.misc.StyleManager
import java.util.*

class ClipCard(type: LayerBox.LayerType) : StackPane() {
    private val contextMenu = ContextMenu()
    private val clipID = UUID.randomUUID().toString()
    private var isSelected = false

    init {
        contextMenu.isAutoFix = true
        contextMenu.isAutoHide = true
        setupGeneralContext()

        var color: Color? = null

        color = when (StyleManager.CURRENT_THEME) {
            StyleManager.Theme.DARK -> {
                when (type) {
                    LayerBox.LayerType.AUDIO -> Color.DARKCYAN
                    LayerBox.LayerType.VIDEO -> Color.DARKRED
                }
            }

            StyleManager.Theme.LIGHT -> {
                when (type) {
                    LayerBox.LayerType.AUDIO -> Color.CYAN
                    LayerBox.LayerType.VIDEO -> Color.CRIMSON.saturate()
                }
            }
        }

        val radius = CornerRadii(6.0)
        val background = Background(BackgroundFill(color, radius, Insets.EMPTY))

        minWidth = 325.0
        padding = Insets(2.0)
        alignment = Pos.TOP_LEFT
        cursor = Cursor.HAND

        setBackground(background)
        registerListener(EventType.CLIP_SELECTED, EventAction { dat: Any? ->
            val data = dat as ClipSelectedEventData?
            if (data!!.id != clipID) {
                isSelected = false

                if (border != null)
                {

                border = null
                }

                return@EventAction
            }

            if (data.state) {
                val border =
                    Border(BorderStroke(Color.DODGERBLUE, BorderStrokeStyle.SOLID, radius, BorderStroke.DEFAULT_WIDTHS))

                setBorder(border)
            } else {
                if (border != null) {
                    border = null
                }
            }
            isSelected = data.state
        })

        val clipText = Text("2025-10-11 18-58-52.mp4")

        if (type == LayerBox.LayerType.AUDIO) {
            setupAudioContext()

            val testResource = javaClass.getResourceAsStream("/test.png")
            val image = Image(testResource)
            val view = ImageView(image)
            val adjust = ColorAdjust()
            adjust.brightness = (if (StyleManager.CURRENT_THEME == StyleManager.Theme.DARK) 0 else -1).toDouble()

            view.isCache = true
            view.cacheHint = CacheHint.SPEED
            view.isSmooth = true
            view.opacity = 0.65
            view.fitHeight = 80.0
            view.isPreserveRatio = true
            view.effect = adjust

            children.add(view)
        } else if (type == LayerBox.LayerType.VIDEO) {
            setupVideoContext()
        }

        onContextMenuRequested = EventHandler { event: ContextMenuEvent? ->
            contextMenu.show(
                this,
                event!!.sceneX,
                event.screenY
            )
        }

        onMouseClicked = EventHandler { event: MouseEvent? ->
            if (event!!.button != MouseButton.PRIMARY) {
                return@EventHandler
            }

            isSelected = !isSelected

            callEvent(EventType.CLIP_SELECTED, ClipSelectedEventData(clipID, isSelected))
            contextMenu.hide()
        }

        children.add(clipText)
    }

    private fun setupGeneralContext() {
        val cutClip = MenuItem("Cut")
        cutClip.graphic = FontIcon.of(FluentUiFilledAL.CUT_24)

        val copyClip = MenuItem("Copy")
        copyClip.graphic = FontIcon.of(FluentUiFilledAL.COPY_24)

        val pasteClip = MenuItem("Paste")
        pasteClip.graphic = FontIcon.of(FluentUiFilledAL.CLIPBOARD_PASTE_24)
        pasteClip.isDisable = true

        val deleteClip = MenuItem("Delete")
        deleteClip.graphic = FontIcon.of(FluentUiFilledAL.DELETE_24)

        val renameClip = MenuItem("Rename Clip")
        renameClip.graphic = FontIcon.of(FluentUiFilledMZ.RENAME_24)

        val setColorClip = MenuItem("Set Clip Color")
        setColorClip.graphic = FontIcon.of(FluentUiFilledAL.COLOR_24)

        contextMenu.items.addAll(
            cutClip,
            copyClip,
            pasteClip,
            deleteClip,
            SeparatorMenuItem(),
            setColorClip,
            renameClip
        )
    }

    private fun setupAudioContext() {
        val changeVolume = MenuItem("Adjust Volume")
        changeVolume.graphic = FontIcon.of(FluentUiFilledMZ.SPEAKER_EDIT_24)

        contextMenu.items.addAll(
            SeparatorMenuItem(),
            changeVolume
        )
    }

    private fun setupVideoContext() {
        contextMenu.items.addAll()
    }
}
