package turboedit.editor.panes.file_browser

import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.input.DragEvent
import javafx.scene.input.TransferMode
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import turboedit.editor.events.EventSystem.EventAction
import turboedit.editor.events.EventSystem.registerListener
import turboedit.editor.events.EventType
import turboedit.editor.events.ThemeChangedEventData
import turboedit.editor.misc.Locale
import turboedit.editor.misc.StyleManager
import turboedit.editor.misc.StyleManager.callEvent
import turboedit.editor.windows.errors.FileErrorWindow
import turboedit.shared.project.ProjectFile
import java.io.IOException
import java.nio.file.Files

class FileBrowserPane : HBox() {
    init {
        val gridGap = 6.0
        val width = 1050.0
        val height = 800.0

        val flow = FlowPane()

        flow.minWidth = width
        flow.minHeight = height

        registerListener(EventType.THEME_CHANGED, EventAction { dat: Any? ->
            val data = dat as ThemeChangedEventData?
            val color = when (data!!.theme) {
                StyleManager.Theme.DARK -> Color.color(1.0, 1.0, 1.0, 0.05)
                StyleManager.Theme.LIGHT -> Color.color(0.0, 0.0, 0.0, 0.05)
                else -> {}
            }

            val background = Background(BackgroundFill(color as Paint?, CornerRadii(10.0), Insets.EMPTY))
            flow.background = background
        })

        callEvent()

        onDragOver = EventHandler { event: DragEvent -> event.acceptTransferModes(TransferMode.LINK) }
        onDragDropped = EventHandler { event: DragEvent ->
            val dragboard = event.dragboard
            val unsupportedFiles = ArrayList<String?>()

            for (file in dragboard.files) {
                try {
                    val mimeType = Files.probeContentType(file.toPath())

                    if (!mimeType.startsWith("video/") && !mimeType.startsWith("audio/")) {
                        unsupportedFiles.add(file.absolutePath)
                        continue
                    }
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }

                println(file.getAbsoluteFile())
            }
            if (!unsupportedFiles.isEmpty()) {
                FileErrorWindow(
                    Locale.getText("title.error.file.unsupported"),
                    Locale.getText("error.file.unsupported"),
                    unsupportedFiles
                )
            }
        }

        flow.children
            .add(FileCard(ProjectFile(
                name = "Test.mp4",
                path = "",
                type = "video/mp4",
                previewImage = null,
                hash = "",
                size = 0,
                isVideo = true,
                videoWidth = 1920,
                videoHeight = 1080,
                videoFPS = 60,
                videoCodec = "AV1",
                audio = null
            )))
        flow.padding = Insets(15.0)
        flow.vgap = gridGap
        flow.hgap = gridGap

        padding = Insets(15.0)
        spacing = 6.0

        children.add(FileActions())
        children.add(flow)
    }
}
