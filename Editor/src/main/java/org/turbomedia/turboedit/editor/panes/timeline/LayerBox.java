package org.turbomedia.turboedit.editor.panes.timeline;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.kordamp.ikonli.fluentui.FluentUiFilledAL;
import org.kordamp.ikonli.fluentui.FluentUiFilledMZ;
import org.kordamp.ikonli.javafx.FontIcon;
import org.turbomedia.turboedit.editor.misc.StyleManager;

import static org.turbomedia.turboedit.editor.misc.Locale.GetText;

public class LayerBox extends VBox {
    public LayerBox(LayerType type) {
        var FONT_NAME = Font.getDefault().getName();

        var buttonBox = new HBox();
        var textBox = new HBox();

        var icon = new Text();
        var layerName = "";

        switch (type) {
            case AUDIO -> {
                layerName = "Audio 1";
                icon = FontIcon.of(FluentUiFilledMZ.SPEAKER_24);
            }
            case VIDEO -> {
                layerName = "Video 1";
                icon = FontIcon.of(FluentUiFilledMZ.VIDEO_24);
            }
        }

        icon.setTranslateY(2);

        var layerLabel = new Text(layerName);

        textBox.getChildren().add(icon);
        textBox.getChildren().add(layerLabel);

        layerLabel.setTextAlignment(TextAlignment.LEFT);
        layerLabel.setFont(Font.font(FONT_NAME, FontWeight.BLACK, 16));

        var lockButton = new Button();
        lockButton.setCursor(Cursor.HAND);
        lockButton.setGraphic(FontIcon.of(FluentUiFilledMZ.UNLOCK_24));
        lockButton.setTooltip(new Tooltip(GetText("timeline.layer.general.lock")));

        buttonBox.getChildren().add(lockButton);

        if (type == LayerType.VIDEO) {
            var visbileButton = new Button();
            visbileButton.setCursor(Cursor.HAND);
            visbileButton.setGraphic(FontIcon.of(FluentUiFilledAL.EYE_SHOW_24));
            visbileButton.setTooltip(new Tooltip(GetText("timeline.layer.video.visible")));

            buttonBox.getChildren().add(visbileButton);
        }

        if (type == LayerType.AUDIO) {
            var soloButton = new Button();
            soloButton.setCursor(Cursor.HAND);
            soloButton.setGraphic(FontIcon.of(FluentUiFilledMZ.SPEAKER_NONE_24));
            soloButton.setTooltip(new Tooltip(GetText("timeline.layer.audio.solo.disabled")));

            var muteButton = new Button();
            muteButton.setCursor(Cursor.HAND);
            muteButton.setGraphic(FontIcon.of(FluentUiFilledMZ.SPEAKER_OFF_24));
            muteButton.setTooltip(new Tooltip(GetText("timeline.layer.audio.mute.disabled")));

            var editButton = new Button();
            editButton.setCursor(Cursor.HAND);
            editButton.setGraphic(FontIcon.of(FluentUiFilledAL.EDIT_24));
            editButton.setTooltip(new Tooltip(GetText("timeline.layer.audio.edit")));

            buttonBox.getChildren().add(editButton);
            buttonBox.getChildren().add(soloButton);
            buttonBox.getChildren().add(muteButton);
        }

        buttonBox.setSpacing(6);
        textBox.setSpacing(6);

        getChildren().add(textBox);
        getChildren().add(buttonBox);

        StyleManager.RegisterEvent((theme) -> {
            Color color = switch (theme) {
                case DARK -> Color.color(1, 1, 1, 0.05);
                case LIGHT -> Color.color(0, 0, 0, 0.05);
            };

            var background = new Background(new BackgroundFill(color, new CornerRadii(10), Insets.EMPTY));

            setBackground(background);
        });

        setSpacing(8);
        setPadding(new Insets(8));
        setMinWidth(280);
        setMaxWidth(280);
    }

    public enum LayerType {
        VIDEO,
        AUDIO
    }
}
