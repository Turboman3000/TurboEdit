package turboedit.editor.panes.file_browser

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.CacheHint
import javafx.scene.Cursor
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.ContextMenuEvent
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.text.Font
import javafx.scene.text.FontSmoothingType
import javafx.scene.text.Text
import org.kordamp.ikonli.fluentui.FluentUiFilledAL
import org.kordamp.ikonli.fluentui.FluentUiFilledMZ
import org.kordamp.ikonli.javafx.FontIcon
import turboedit.editor.events.EventSystem.EventAction
import turboedit.editor.events.EventSystem.registerListener
import turboedit.editor.events.EventType
import turboedit.editor.events.ThemeChangedEventData
import turboedit.editor.misc.StyleManager
import turboedit.editor.windows.file_browser.FileDetailsWindow
import turboedit.shared.project.ProjectFile

class FileCard(private val file: ProjectFile) : VBox() {
    private val contextMenu = ContextMenu()

    init {
        initContextMenu()

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

        val resource = javaClass.getResource("/test2.png")
        val stream = resource!!.openStream()
        val previewImage = Image(stream)
        val preview = ImageView(previewImage)

        preview.isPreserveRatio = true
        preview.fitWidth = 160.0
        preview.isCache = true
        preview.cacheHint = CacheHint.SPEED

        val fileName = Text(file.name)
        fileName.fontSmoothingType = FontSmoothingType.LCD
        fileName.font = Font.font(fontName, 12.0)

        cursor = Cursor.HAND
        children.add(preview)
        children.add(fileName)
        spacing = 6.0
        padding = Insets(6.0)
        onContextMenuRequested = EventHandler { e: ContextMenuEvent? ->
            contextMenu.show(
                this,
                e!!.screenX,
                e.screenY
            )
        }
        onMouseClicked = EventHandler { e: MouseEvent? ->
            if (e!!.button == MouseButton.PRIMARY && e.isAltDown) {
                FileDetailsWindow(file)
            }
        }
    }

    private fun initContextMenu() {
        val removeFile = MenuItem("Remove File")
        removeFile.graphic = FontIcon.of(FluentUiFilledAL.DISMISS_24)

        val replaceFile = MenuItem("Replace File")
        replaceFile.graphic = FontIcon.of(FluentUiFilledAL.LINK_24)

        val fileDetails = MenuItem("File Details")
        fileDetails.graphic = FontIcon.of(FluentUiFilledAL.APPS_LIST_24)
        fileDetails.onAction = EventHandler { e: ActionEvent? -> FileDetailsWindow(file) }

        val transcode = MenuItem("Transcode")
        transcode.isDisable = true
        transcode.graphic = FontIcon.of(FluentUiFilledMZ.VIDEO_CLIP_24)

        val transcribe = MenuItem("Transcribe")
        transcribe.isDisable = true
        transcribe.graphic = FontIcon.of(FluentUiFilledMZ.TEXTBOX_24)

        val proxySettings = MenuItem("Manage Proxy Settings")
        proxySettings.isDisable = true
        proxySettings.graphic = FontIcon.of(FluentUiFilledAL.DOCUMENT_EDIT_24)

        contextMenu.isAutoHide = true
        contextMenu.isAutoFix = true
        contextMenu.items.addAll(removeFile, replaceFile, fileDetails, transcode, transcribe, proxySettings)
    }
}
