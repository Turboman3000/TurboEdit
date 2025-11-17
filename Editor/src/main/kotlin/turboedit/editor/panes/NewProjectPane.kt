package turboedit.editor.panes

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.input.KeyEvent
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.DirectoryChooser
import javafx.stage.Stage
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import turboedit.editor.Editor
import turboedit.editor.ProjectManager
import turboedit.editor.ProjectManager.CURRENT_VERSION
import turboedit.editor.ProjectManager.loadProject
import turboedit.editor.misc.Locale
import turboedit.shared.project.Project
import turboedit.shared.project.ProjectFile
import turboedit.shared.project.ProjectWriter
import turboedit.shared.project.TimelineData
import java.io.File
import java.io.IOException
import java.nio.file.Path
import javax.swing.filechooser.FileSystemView

class NewProjectPane(stage: Stage, parentStage: Stage?) : GridPane() {
    private var currentBasePath: String
    private var currentPath: String
    private var currentName = Locale.getText("new_project.textfield.name.placeholder")

    private val pathTextField = TextField()
    private val createButton = Button(Locale.getText("new_project.button.create"))
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    init {
        currentBasePath = FileSystemView.getFileSystemView().getDefaultDirectory().path

        hgap = 10.0
        vgap = 10.0
        alignment = Pos.CENTER

        val nameFieldLabel = Label(Locale.getText("new_project.textfield.name.label"))

        val nameField = TextField(Locale.getText("new_project.textfield.name.placeholder"))
        nameField.onKeyTyped = EventHandler { event: KeyEvent? ->
            val text = nameField.text
            createButton.isDisable = text.isBlank()

            currentName = text.replace(" ", "_")
            currentPath = Path.of(currentBasePath, currentName)
                .toString() + (if (text.isBlank()) "" else ProjectManager.EXTENSION)

            pathTextField.text = currentPath
        }
        nameField.setPrefSize(400.0, 20.0)

        add(nameFieldLabel, 0, 0, 1, 1)
        add(nameField, 0, 1, 1, 1)

        currentPath =
            Path.of(currentBasePath, nameField.text.replace(" ", "_")).toString() + ProjectManager.EXTENSION
        pathTextField.text = currentPath

        add(getPathBox(stage), 0, 2, 1, 1)
        add(getButtonBox(stage, parentStage), 0, 3, 1, 1)
    }

    private fun getPathBox(stage: Stage?): Node {
        val label = Label(Locale.getText("new_project.textfield.path.label"))
        val button = Button("...")

        button.onAction = EventHandler { event: ActionEvent? ->
            val chooser = DirectoryChooser()
            chooser.title = Editor.Companion.TITLE + " - " + Locale.getText("title.select_folder")
            chooser.initialDirectory = File(currentBasePath)

            val result = chooser.showDialog(stage)

            if (result == null) return@EventHandler

            currentBasePath = result.path
            currentPath = Path.of(result.path, currentName)
                .toString() + (if (currentName.isBlank()) "" else ProjectManager.EXTENSION)

            pathTextField.text = currentPath
        }

        pathTextField.isDisable = true
        pathTextField.setPrefSize(
            360.0,
            20.0
        )

        val vbox = VBox(label, pathTextField)
        vbox.spacing = 10.0

        val hbox = HBox(vbox, button)
        hbox.spacing = 10.0
        hbox.alignment = Pos.BOTTOM_CENTER

        return hbox
    }

    private fun getButtonBox(stage: Stage, parentStage: Stage?): Node {
        createButton.onAction = EventHandler { event: ActionEvent? ->
            val project = Project(
                currentPath,
                currentName,
                CURRENT_VERSION,
                ArrayList(),
                ArrayList()
            )
            try {
                ProjectWriter.write(project)
            } catch (e: IOException) {
                throw RuntimeException(e)
            }

            stage.close()
            try {
                loadProject(File(currentPath))
            } catch (e: IOException) {
                logger.error("{} = {}", e.javaClass.getName(), e.message)
            }
        }

        val closeButton = Button(Locale.getText("new_project.button.close"))
        closeButton.onAction = EventHandler { event: ActionEvent? -> stage.close() }

        val buttonBox = HBox(createButton, closeButton)
        buttonBox.alignment = Pos.BASELINE_RIGHT
        buttonBox.spacing = 10.0

        return buttonBox
    }
}
