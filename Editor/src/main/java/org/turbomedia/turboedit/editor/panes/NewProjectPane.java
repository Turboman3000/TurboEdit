package org.turbomedia.turboedit.editor.panes;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.turbomedia.turboedit.editor.ProjectManager;
import org.turbomedia.turboedit.shared.project.Project;
import org.turbomedia.turboedit.shared.project.ProjectWriter;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.turbomedia.turboedit.editor.Editor.TITLE;
import static org.turbomedia.turboedit.editor.ProjectManager.CURRENT_VERSION;
import static org.turbomedia.turboedit.editor.misc.Locale.GetText;

public class NewProjectPane extends GridPane {
    private String currentBasePath;
    private String currentPath;
    private String currentName;

    private final TextField pathTextField = new TextField();
    private final Button createButton = new Button(GetText("new_project.button.create"));
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public NewProjectPane(Stage stage, Stage parentStage) {
        currentBasePath = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();

        setHgap(10);
        setVgap(10);
        setAlignment(Pos.CENTER);

        var nameFieldLabel = new Label(GetText("new_project.textfield.name.label"));
        var nameField = new TextField(GetText("new_project.textfield.name.placeholder"));
        nameField.setOnKeyTyped(event -> {
            var text = nameField.getText();

            createButton.setDisable(text.isBlank());

            currentName = text.replace(" ", "_");
            currentPath = Path.of(currentBasePath, currentName) + (text.isBlank() ? "" : ProjectManager.EXTENSION);

            pathTextField.setText(currentPath);
        });
        nameField.setPrefSize(400, 20);

        add(nameFieldLabel, 0, 0, 1, 1);
        add(nameField, 0, 1, 1, 1);

        currentPath = Path.of(currentBasePath, nameField.getText().replace(" ", "_")) + ProjectManager.EXTENSION;
        pathTextField.setText(currentPath);

        add(getPathBox(stage), 0, 2, 1, 1);
        add(getButtonBox(stage, parentStage), 0, 3, 1, 1);
    }

    private Node getPathBox(Stage stage) {
        var label = new Label(GetText("new_project.textfield.path.label"));
        var button = new Button("...");

        button.setOnAction(event -> {
            var chooser = new DirectoryChooser();

            chooser.setTitle(TITLE + " - " + GetText("title.select_folder"));
            chooser.setInitialDirectory(new File(currentBasePath));

            var result = chooser.showDialog(stage);

            if (result == null) return;

            currentBasePath = result.getPath();
            currentPath = Path.of(result.getPath(), currentName) + (currentName.isBlank() ? "" : ProjectManager.EXTENSION);

            pathTextField.setText(currentPath);
        });

        pathTextField.setDisable(true);
        pathTextField.setPrefSize(360, 20);

        var vbox = new VBox(label, pathTextField);
        vbox.setSpacing(10);

        var hbox = new HBox(vbox, button);
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.BOTTOM_CENTER);

        return hbox;
    }

    private Node getButtonBox(Stage stage, Stage parentStage) {
        createButton.setOnAction(event -> {
            var project = new Project(currentPath, currentName, CURRENT_VERSION, new ArrayList<>(), new ArrayList<>());

            try {
                ProjectWriter.Write(project);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            stage.close();

            try {
                ProjectManager.LoadProject(new File(currentPath), parentStage);
            } catch (IOException e) {
                logger.error("{} = {}", e.getClass().getName(), e.getMessage());
            }
        });

        var closeButton = new Button(GetText("new_project.button.close"));
        closeButton.setOnAction(event -> stage.close());

        var buttonBox = new HBox(createButton, closeButton);
        buttonBox.setAlignment(Pos.BASELINE_RIGHT);
        buttonBox.setSpacing(10);

        return buttonBox;
    }
}
