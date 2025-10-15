package org.turbomedia.turboedit.editor.panes.preferences;

import atlantafx.base.controls.Spacer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.turbomedia.turboedit.editor.misc.PreferencesFile;
import org.turbomedia.turboedit.editor.renderer.FileResolveMethod;
import org.turbomedia.turboedit.editor.renderer.RenderServerEntry;
import org.turbomedia.turboedit.editor.windows.SimpleMessageBox;

import java.io.IOException;

public class AddRenderServerPane extends VBox {
    public AddRenderServerPane(Stage stage) {
        setPadding(new Insets(15));
        setSpacing(6);

        var serverNameLabel = new Label("Server Display Name");
        var serverNameInput = new TextField();
        serverNameLabel.setLabelFor(serverNameInput);

        var serverIPLabel = new Label("Server IP");
        var serverIPInput = new TextField("127.0.0.1");
        serverIPLabel.setLabelFor(serverIPInput);

        var fileModeLabel = new Label("File Resolving Method");
        var fileModeBox = new ComboBox<FileResolveMethod>();

        for (var item : FileResolveMethod.values()) {
            fileModeBox.getItems().add(item);
        }

        fileModeBox.setValue(FileResolveMethod.STREAMING);
        fileModeBox.setMaxWidth(Double.MAX_VALUE);
        fileModeBox.setCursor(Cursor.HAND);
        fileModeLabel.setLabelFor(fileModeBox);

        var defaultCheckbox = new CheckBox("Default Server");
        defaultCheckbox.setCursor(Cursor.HAND);

        var addButton = new Button("Add");
        addButton.setCursor(Cursor.HAND);
        addButton.setOnAction((event) -> {
            if (serverNameInput.getText().isBlank()) {
                new SimpleMessageBox("Error", "Server name can't be blank!");
                return;
            }

            var entry = new RenderServerEntry(serverNameInput.getText(), serverIPInput.getText(), fileModeBox.getValue(), defaultCheckbox.isSelected(), false);

            try {
                PreferencesFile.CURRENT_PREFERENCES.addRenderServers(entry);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }

            stage.close();
        });

        var closeButton = new Button("Close");
        closeButton.setCursor(Cursor.HAND);
        closeButton.setOnAction((event) -> stage.close());

        var actionBox = new HBox(defaultCheckbox, new Spacer(), addButton, closeButton);
        actionBox.setAlignment(Pos.CENTER);
        actionBox.setSpacing(6);

        getChildren().addAll(serverNameLabel, serverNameInput, serverIPLabel, serverIPInput, fileModeLabel, fileModeBox, actionBox);
    }
}
