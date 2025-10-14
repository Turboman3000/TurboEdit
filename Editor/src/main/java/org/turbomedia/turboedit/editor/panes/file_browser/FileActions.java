package org.turbomedia.turboedit.editor.panes.file_browser;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.fluentui.FluentUiFilledAL;
import org.kordamp.ikonli.fluentui.FluentUiFilledMZ;
import org.kordamp.ikonli.javafx.FontIcon;
import org.turbomedia.turboedit.editor.events.EventSystem;
import org.turbomedia.turboedit.editor.events.EventType;
import org.turbomedia.turboedit.editor.events.ThemeChangedEventData;

import static org.turbomedia.turboedit.editor.misc.Locale.GetText;

public class FileActions extends VBox {
    public FileActions() {
        EventSystem.RegisterListener(EventType.THEME_CHANGED, (dat) -> {
            var data = (ThemeChangedEventData) dat;

            Color color = switch (data.theme()) {
                case DARK -> Color.color(1, 1, 1, 0.05);
                case LIGHT -> Color.color(0, 0, 0, 0.05);
            };

            var background = new Background(new BackgroundFill(color, new CornerRadii(10), Insets.EMPTY));

            setBackground(background);
        });

        var renameButton = new Button();
        renameButton.setTooltip(new Tooltip(GetText("file_browser.action.rename")));
        renameButton.setCursor(Cursor.HAND);
        renameButton.setDisable(true);
        renameButton.setGraphic(FontIcon.of(FluentUiFilledMZ.RENAME_24));

        var deleteButton = new Button();
        deleteButton.setTooltip(new Tooltip(GetText("file_browser.action.delete")));
        deleteButton.setCursor(Cursor.HAND);
        deleteButton.setDisable(true);
        deleteButton.setGraphic(FontIcon.of(FluentUiFilledAL.DELETE_24));

        var reloadButton = new Button();
        reloadButton.setTooltip(new Tooltip(GetText("file_browser.action.reload")));
        reloadButton.setCursor(Cursor.HAND);
        reloadButton.setGraphic(FontIcon.of(FluentUiFilledAL.ARROW_CLOCKWISE_24));

        var addFolderButton = new Button();
        addFolderButton.setTooltip(new Tooltip(GetText("file_browser.action.add_folder")));
        addFolderButton.setCursor(Cursor.HAND);
        addFolderButton.setGraphic(FontIcon.of(FluentUiFilledAL.FOLDER_ADD_24));

        var addButton = new Button();
        addButton.setTooltip(new Tooltip(GetText("file_browser.action.add")));
        addButton.setCursor(Cursor.HAND);
        addButton.setGraphic(FontIcon.of(FluentUiFilledAL.DOCUMENT_ADD_24));

        var sortButton = new MenuButton();
        sortButton.setTooltip(new Tooltip(GetText("file_browser.action.sort")));
        sortButton.setCursor(Cursor.HAND);
        sortButton.setGraphic(FontIcon.of(FluentUiFilledAL.ARROW_SORT_24));
        sortButton.setPopupSide(Side.TOP);

        {
            var sort1 = new MenuItem(GetText("file_browser.action.sort.name_asc"));
            var sort2 = new MenuItem(GetText("file_browser.action.sort.name_desc"));

            var sort3 = new MenuItem(GetText("file_browser.action.sort.type_asc"));
            var sort4 = new MenuItem(GetText("file_browser.action.sort.type_desc"));

            sortButton.getItems().addAll(sort1, sort2, sort3, sort4);
        }

        getChildren().addAll(renameButton, deleteButton, reloadButton, addButton, addFolderButton, sortButton);

        VBox.setVgrow(this, Priority.ALWAYS);

        setAlignment(Pos.BOTTOM_CENTER);
        setMaxHeight(Double.MAX_VALUE);
        setSpacing(6);
        setPadding(new Insets(6));
    }
}
