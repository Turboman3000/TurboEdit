package org.turbomedia.turboedit.editor.panes.timeline;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.kordamp.ikonli.fluentui.FluentUiFilledAL;
import org.kordamp.ikonli.fluentui.FluentUiFilledMZ;
import org.kordamp.ikonli.javafx.FontIcon;
import org.turbomedia.turboedit.editor.events.ClipSelectedEventData;
import org.turbomedia.turboedit.editor.events.EventSystem;
import org.turbomedia.turboedit.editor.events.EventType;
import org.turbomedia.turboedit.editor.misc.StyleManager;

import java.io.IOException;
import java.util.UUID;

public class ClipCard extends StackPane {
    private final ContextMenu contextMenu = new ContextMenu();
    private String clipID = UUID.randomUUID().toString();
    private boolean isSelected;

    public ClipCard(LayerBox.LayerType type) throws IOException {
        contextMenu.setAutoFix(true);
        contextMenu.setAutoHide(true);
        setupGeneralContext();

        Color color = null;

        switch (StyleManager.CURRENT_THEME) {
            case DARK -> {
                switch (type) {
                    case AUDIO -> color = Color.DARKCYAN;
                    case VIDEO -> color = Color.DARKRED;
                }
            }
            case LIGHT -> {
                switch (type) {
                    case AUDIO -> color = Color.CYAN;
                    case VIDEO -> color = Color.CRIMSON.saturate();
                }
            }
        }

        var radius = new CornerRadii(6);
        var background = new Background(new BackgroundFill(color, radius, Insets.EMPTY));

        setBackground(background);
        setMinWidth(325);
        setPadding(new Insets(2));
        setAlignment(Pos.TOP_LEFT);
        setCursor(Cursor.HAND);

        EventSystem.RegisterListener(EventType.CLIP_SELECTED, (dat) -> {
            var data = (ClipSelectedEventData) dat;

            if (!data.id().equals(clipID)) {
                isSelected = false;

                if (getBorder() == null) return;
                setBorder(null);

                return;
            }

            if (data.state()) {
                var border = new Border(new BorderStroke(Color.DODGERBLUE, BorderStrokeStyle.SOLID, radius, BorderStroke.DEFAULT_WIDTHS));

                setBorder(border);
            } else {
                if (getBorder() != null) {
                    setBorder(null);
                }
            }

            isSelected = data.state();
        });

        var clipText = new Text("2025-10-11 18-58-52.mp4");

        if (type == LayerBox.LayerType.AUDIO) {
            setupAudioContext();

            var testResource = getClass().getResourceAsStream("/test.png");
            var image = new Image(testResource);
            var view = new ImageView(image);
            var adjust = new ColorAdjust();
            adjust.setBrightness(StyleManager.CURRENT_THEME == StyleManager.Theme.DARK ? 0 : -1);

            view.setCache(true);
            view.setCacheHint(CacheHint.SPEED);

            view.setSmooth(true);
            view.setOpacity(0.65);
            view.setFitHeight(80);
            view.setPreserveRatio(true);
            view.setEffect(adjust);

            getChildren().add(view);
        } else if (type == LayerBox.LayerType.VIDEO) {
            setupVideoContext();
        }

        setOnContextMenuRequested((event) -> contextMenu.show(this, event.getSceneX(), event.getScreenY()));

        setOnMouseClicked((event) -> {
            if (event.getButton() != MouseButton.PRIMARY) {
                return;
            }

            isSelected = !isSelected;

            EventSystem.CallEvent(EventType.CLIP_SELECTED, new ClipSelectedEventData(clipID, isSelected));

            contextMenu.hide();
        });

        getChildren().add(clipText);
    }

    private void setupGeneralContext() {
        var cutClip = new MenuItem("Cut");
        cutClip.setGraphic(FontIcon.of(FluentUiFilledAL.CUT_24));

        var copyClip = new MenuItem("Copy");
        copyClip.setGraphic(FontIcon.of(FluentUiFilledAL.COPY_24));

        var pasteClip = new MenuItem("Paste");
        pasteClip.setGraphic(FontIcon.of(FluentUiFilledAL.CLIPBOARD_PASTE_24));
        pasteClip.setDisable(true);

        var deleteClip = new MenuItem("Delete");
        deleteClip.setGraphic(FontIcon.of(FluentUiFilledAL.DELETE_24));

        var renameClip = new MenuItem("Rename Clip");
        renameClip.setGraphic(FontIcon.of(FluentUiFilledMZ.RENAME_24));

        var setColorClip = new MenuItem("Set Clip Color");
        setColorClip.setGraphic(FontIcon.of(FluentUiFilledAL.COLOR_24));

        contextMenu.getItems().addAll(
                cutClip,
                copyClip,
                pasteClip,
                deleteClip,
                new SeparatorMenuItem(),
                setColorClip,
                renameClip
        );
    }

    private void setupAudioContext() {
        var changeVolume = new MenuItem("Adjust Volume");
        changeVolume.setGraphic(FontIcon.of(FluentUiFilledMZ.SPEAKER_EDIT_24));

        contextMenu.getItems().addAll(
                new SeparatorMenuItem(),
                changeVolume
        );
    }

    private void setupVideoContext() {
        contextMenu.getItems().addAll();
    }
}
