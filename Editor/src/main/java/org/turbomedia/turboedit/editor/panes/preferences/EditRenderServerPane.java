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

import static org.turbomedia.turboedit.editor.misc.Locale.GetText;

public class EditRenderServerPane extends VBox {
    public EditRenderServerPane(Stage stage, RenderServerEntry entry) {
        setPadding(new Insets(15));
        setSpacing(6);

        var alreadyExists = PreferencesFile.CURRENT_PREFERENCES.renderServers.contains(entry);

        var serverNameLabel = new Label(GetText("edit_render_server.display_name"));
        var serverNameInput = new TextField(entry.displayName());
        serverNameLabel.setLabelFor(serverNameInput);
        serverNameInput.setDisable(entry.buildIn());

        var serverIPLabel = new Label(GetText("edit_render_server.ip"));
        var serverIPInput = new TextField(entry.ip());
        serverIPLabel.setLabelFor(serverIPInput);
        serverIPInput.setDisable(entry.buildIn());

        var fileModeLabel = new Label(GetText("edit_render_server.file_resolve"));
        var fileModeBox = new ComboBox<FileResolveMethod>();

        for (var item : FileResolveMethod.values()) {
            fileModeBox.getItems().add(item);
        }

        fileModeBox.setDisable(entry.buildIn());
        fileModeBox.setValue(entry.fileResolveMethod());
        fileModeBox.setMaxWidth(Double.MAX_VALUE);
        fileModeBox.setCursor(Cursor.HAND);
        fileModeLabel.setLabelFor(fileModeBox);

        var defaultCheckbox = new CheckBox(GetText("edit_render_server.default_server"));
        defaultCheckbox.setCursor(Cursor.HAND);
        defaultCheckbox.setSelected(entry.defaultServer());

        var addButton = new Button(GetText("edit_render_server.button." + (alreadyExists ? "edit" : "add")));
        addButton.setCursor(Cursor.HAND);
        addButton.setOnAction((event) -> {
            if (serverNameInput.getText().isBlank()) {
                new SimpleMessageBox(GetText("edit_render_server.error.no_display_name.title"), GetText("edit_render_server.error.no_display_name"));
                return;
            }

            var newEntry = new RenderServerEntry(serverNameInput.getText(), serverIPInput.getText(), fileModeBox.getValue(), defaultCheckbox.isSelected(), false);

            try {
                if (alreadyExists) {
                    PreferencesFile.CURRENT_PREFERENCES.removeRenderServers(entry);
                }

                PreferencesFile.CURRENT_PREFERENCES.addRenderServers(newEntry);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }

            stage.close();
        });

        var closeButton = new Button(GetText("edit_render_server.button.close"));
        closeButton.setCursor(Cursor.HAND);
        closeButton.setOnAction((event) -> stage.close());

        var actionBox = new HBox(defaultCheckbox, new Spacer(), addButton, closeButton);
        actionBox.setAlignment(Pos.CENTER);
        actionBox.setSpacing(6);

        getChildren().addAll(serverNameLabel, serverNameInput, serverIPLabel, serverIPInput, fileModeLabel, fileModeBox, actionBox);
    }
}
