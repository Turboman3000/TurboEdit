package org.turbomedia.turboedit.editor.panes.file_browser;

import javafx.geometry.Insets;
import javafx.scene.CacheHint;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import org.kordamp.ikonli.fluentui.FluentUiFilledAL;
import org.kordamp.ikonli.fluentui.FluentUiFilledMZ;
import org.kordamp.ikonli.javafx.FontIcon;
import org.turbomedia.turboedit.editor.events.EventSystem;
import org.turbomedia.turboedit.editor.events.EventType;
import org.turbomedia.turboedit.editor.events.ThemeChangedEventData;
import org.turbomedia.turboedit.editor.windows.file_browser.FileDetailsWindow;
import org.turbomedia.turboedit.shared.project.ProjectFile;

import java.io.IOException;

public class FileCard extends VBox {
    private final ContextMenu contextMenu = new ContextMenu();
    private final ProjectFile file;

    public FileCard(ProjectFile file) throws IOException {
        this.file = file;

        initContextMenu();

        var FONT_NAME = Font.getDefault().getName();

        EventSystem.RegisterListener(EventType.THEME_CHANGED, (dat) -> {
            var data = (ThemeChangedEventData) dat;

            Color color = switch (data.theme()) {
                case DARK -> Color.color(1, 1, 1, 0.05);
                case LIGHT -> Color.color(0, 0, 0, 0.05);
            };

            var background = new Background(new BackgroundFill(color, new CornerRadii(10), Insets.EMPTY));

            setBackground(background);
        });

        var resource = getClass().getResource("/test2.png");
        var stream = resource.openStream();
        var previewImage = new Image(stream);
        var preview = new ImageView(previewImage);

        preview.setPreserveRatio(true);
        preview.setFitWidth(160);
        preview.setCache(true);
        preview.setCacheHint(CacheHint.SPEED);

        var fileName = new Text(file.name());
        fileName.setFontSmoothingType(FontSmoothingType.LCD);
        fileName.setFont(Font.font(FONT_NAME, 12));

        setCursor(Cursor.HAND);
        getChildren().add(preview);
        getChildren().add(fileName);
        setSpacing(6);
        setPadding(new Insets(6));
        setOnContextMenuRequested((e) -> contextMenu.show(this, e.getScreenX(), e.getScreenY()));
        setOnMouseClicked((e) -> {
            if (e.getButton() == MouseButton.PRIMARY && e.isAltDown()) {
                new FileDetailsWindow(file);
            }
        });
    }

    private void initContextMenu() {
        var removeFile = new MenuItem("Remove File");
        removeFile.setGraphic(FontIcon.of(FluentUiFilledAL.DISMISS_24));

        var replaceFile = new MenuItem("Replace File");
        replaceFile.setGraphic(FontIcon.of(FluentUiFilledAL.LINK_24));

        var fileDetails = new MenuItem("File Details");
        fileDetails.setGraphic(FontIcon.of(FluentUiFilledAL.APPS_LIST_24));
        fileDetails.setOnAction((e) -> new FileDetailsWindow(file));

        var transcode = new MenuItem("Transcode");
        transcode.setDisable(true);
        transcode.setGraphic(FontIcon.of(FluentUiFilledMZ.VIDEO_CLIP_24));

        var transcribe = new MenuItem("Transcribe");
        transcribe.setDisable(true);
        transcribe.setGraphic(FontIcon.of(FluentUiFilledMZ.TEXTBOX_24));

        var proxySettings = new MenuItem("Manage Proxy Settings");
        proxySettings.setDisable(true);
        proxySettings.setGraphic(FontIcon.of(FluentUiFilledAL.DOCUMENT_EDIT_24));

        contextMenu.setAutoHide(true);
        contextMenu.setAutoFix(true);
        contextMenu.getItems().addAll(removeFile, replaceFile, fileDetails, transcode, transcribe, proxySettings);
    }
}
