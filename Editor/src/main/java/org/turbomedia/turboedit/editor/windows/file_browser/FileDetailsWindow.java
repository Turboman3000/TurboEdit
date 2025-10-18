package org.turbomedia.turboedit.editor.windows.file_browser;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.turbomedia.turboedit.shared.project.ProjectFile;

import static org.turbomedia.turboedit.editor.Editor.ICON;
import static org.turbomedia.turboedit.editor.Editor.TITLE;

public class FileDetailsWindow extends Stage {
    private final ProjectFile file;

    public FileDetailsWindow(ProjectFile file) {
        this.file = file;

        var closeButton = new Button("Close");
        closeButton.setOnAction((e) -> close());

        var box = new HBox(getLabels(), getFields());
        box.setSpacing(8);

        var finalBox = new VBox(box, closeButton);
        finalBox.setPadding(new Insets(15));
        finalBox.setSpacing(6);
        finalBox.setAlignment(Pos.CENTER_RIGHT);

        setScene(new Scene(finalBox));
        setTitle(TITLE + " - " + file.name());
        setResizable(false);
        setAlwaysOnTop(true);
        getIcons().add(ICON);
        centerOnScreen();
        initModality(Modality.APPLICATION_MODAL);
        showAndWait();
    }

    private VBox getLabels() {
        var name = new Label("File Name: ");
        var path = new Label("File Path: ");
        var type = new Label("MIME-Type: ");
        var size = new Label("File Size: ");
        var hash = new Label("File Hash: ");

        var box = new VBox(name, path, type);

        if (file.isVideo()) {
            var fps = new Label("Video FPS: ");
            var resolution = new Label("Video Resolution: ");
            var codec = new Label("Video Codec: ");

            box.getChildren().addAll(fps, resolution, codec);
        }

        box.getChildren().addAll(size, hash);
        box.setPadding(new Insets(8, 0, 0, 0));
        box.setSpacing(22);
        return box;
    }

    private VBox getFields() {
        final double MIN_WIDTH = 400;

        var name = new TextField(file.name());
        var path = new TextField(file.path());
        var type = new TextField(file.type());
        var size = new TextField(file.size() + " GB");
        var hash = new TextField(file.hash());

        var box = new VBox(name, path, type);

        if (file.isVideo()) {
            var fps = new TextField(String.valueOf(file.videoFPS()));
            var resolution = new TextField(file.videoWidth() + " x " + file.videoHeight());
            var codec = new TextField(file.videoCodec());

            box.getChildren().addAll(fps, resolution, codec);
        }

        box.getChildren().addAll(size, hash);

        for (var child : box.getChildren()) {
            ((TextField) child).setEditable(false);
            ((TextField) child).setMinWidth(MIN_WIDTH);
            ((TextField) child).setMaxWidth(MIN_WIDTH);
        }

        box.setSpacing(6);
        return box;
    }
}
