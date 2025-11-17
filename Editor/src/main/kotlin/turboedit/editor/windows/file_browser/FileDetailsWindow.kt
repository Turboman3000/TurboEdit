package turboedit.editor.windows.file_browser

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Modality
import javafx.stage.Stage
import turboedit.editor.Editor
import turboedit.shared.project.ProjectFile

class FileDetailsWindow(private val file: ProjectFile) : Stage() {
    init {
        val closeButton = Button("Close")
        closeButton.onAction = EventHandler { e: ActionEvent? -> close() }

        val box = HBox(this.labels, this.fields)
        box.spacing = 8.0

        val finalBox = VBox(box, closeButton)
        finalBox.padding = Insets(15.0)
        finalBox.spacing = 6.0
        finalBox.alignment = Pos.CENTER_RIGHT

        title = Editor.Companion.TITLE + " - " + file.name
        isResizable = false
        icons.add(Editor.Companion.ICON)
        scene = Scene(finalBox);
        isAlwaysOnTop = true

        centerOnScreen()
        initModality(Modality.APPLICATION_MODAL)
        showAndWait()
    }

    private val labels: VBox
        get() {
            val name = Label("File Name: ")
            val path = Label("File Path: ")
            val type = Label("MIME-Type: ")
            val size = Label("File Size: ")
            val hash = Label("File Hash: ")

            val box = VBox(name, path, type)

            if (file.isVideo) {
                val fps = Label("Video FPS: ")
                val resolution = Label("Video Resolution: ")
                val codec = Label("Video Codec: ")

                box.children.addAll(fps, resolution, codec)
            }

            box.children.addAll(size, hash)
            box.padding = Insets(8.0, 0.0, 0.0, 0.0)
            box.spacing = 22.0

            return box
        }

    private val fields: VBox
        get() {
            val minWidth = 400.0

            val name = TextField(file.name)
            val path = TextField(file.path)
            val type = TextField(file.type)
            val size = TextField(file.size.toString() + " GB")
            val hash = TextField(file.hash)

            val box = VBox(name, path, type)

            if (file.isVideo) {
                val fps = TextField(file.videoFPS.toString())
                val resolution =
                    TextField(file.videoWidth.toString() + " x " + file.videoHeight)
                val codec = TextField(file.videoCodec)

                box.children.addAll(fps, resolution, codec)
            }

            box.children.addAll(size, hash)

            for (child in box.children) {
                (child as TextField).isEditable = false
                child.minWidth = minWidth
                child.maxWidth = minWidth
            }

            box.spacing = 6.0
            return box
        }
}
