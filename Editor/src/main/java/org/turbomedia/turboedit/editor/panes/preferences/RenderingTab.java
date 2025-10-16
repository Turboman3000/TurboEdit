package org.turbomedia.turboedit.editor.panes.preferences;

import atlantafx.base.controls.Spacer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.kordamp.ikonli.fluentui.FluentUiFilledAL;
import org.kordamp.ikonli.javafx.FontIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.turbomedia.turboedit.editor.misc.PreferencesFile;
import org.turbomedia.turboedit.editor.renderer.RenderServerEntry;
import org.turbomedia.turboedit.editor.windows.preferences.AddRenderServerWindow;

import java.io.IOException;

import static org.turbomedia.turboedit.editor.misc.Locale.GetText;

public class RenderingTab extends Tab {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public RenderingTab() {
        var FONT_NAME = Font.getDefault().getName();

        var box = new VBox();
        box.setPadding(new Insets(10));
        box.setSpacing(10);

        {
            var headerText = new Text("Render Servers");
            headerText.setFont(Font.font(FONT_NAME, FontWeight.BOLD, 14));

            var deleteButton = new Button("Delete");
            deleteButton.setCursor(Cursor.HAND);
            deleteButton.setDisable(true);
            deleteButton.setGraphic(FontIcon.of(FluentUiFilledAL.DELETE_24));

            var editButton = new Button("Edit");
            editButton.setCursor(Cursor.HAND);
            editButton.setDisable(true);
            editButton.setGraphic(FontIcon.of(FluentUiFilledAL.EDIT_24));

            var serverList = new ListView<RenderServerEntry>();

            serverList.getItems().addAll(PreferencesFile.CURRENT_PREFERENCES.renderServers);

            var newButton = new Button("Add");
            newButton.setCursor(Cursor.HAND);
            newButton.setGraphic(FontIcon.of(FluentUiFilledAL.ADD_24));
            newButton.setOnAction((event) -> {
                var window = new AddRenderServerWindow();

                window.setOnCloseRequest(windowEvent -> {
                    var size = serverList.getItems().size();

                    for (var x = 0; x < size; x++) {
                        serverList.getItems().remove(x);
                    }

                    serverList.getItems().addAll(PreferencesFile.CURRENT_PREFERENCES.renderServers);
                });
            });

            serverList.setOnMouseClicked((event) -> {
                var items = serverList.getSelectionModel().getSelectedItems();

                if (items.isEmpty()) return;

                var selected = items.getFirst();

                editButton.setDisable(false);
                deleteButton.setDisable(selected.buildIn());
            });

            deleteButton.setOnAction((e) -> {
                var size = serverList.getItems().size();

                for (var x = 0; x < size; x++) {
                    var item = serverList.getItems().get(x);
                    var selected = serverList.getSelectionModel().getSelectedItems().getFirst();

                    if (selected == item) {
                        serverList.getItems().remove(x);

                        try {
                            PreferencesFile.CURRENT_PREFERENCES.removeRenderServers(selected);
                        } catch (IOException | InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        break;
                    }
                }

                serverList.refresh();
            });

            var showIPsCheckbox = new CheckBox("Show IPs");
            showIPsCheckbox.setCursor(Cursor.HAND);
            showIPsCheckbox.setSelected(PreferencesFile.CURRENT_PREFERENCES.showIPsForServers);
            showIPsCheckbox.setOnAction((event) -> {
                try {
                    PreferencesFile.CURRENT_PREFERENCES.showIPsForServers(showIPsCheckbox.isSelected());
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }

                serverList.refresh();
            });

            var actions = new HBox(showIPsCheckbox, new Spacer(), deleteButton, editButton, newButton);
            actions.setSpacing(6);
            actions.setAlignment(Pos.CENTER);

            box.getChildren().add(headerText);
            box.getChildren().add(serverList);
            box.getChildren().add(actions);
        }

        setText(GetText("preferences.rendering.tab"));
        setContent(box);
    }
}
