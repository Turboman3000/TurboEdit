package org.turbomedia.turboedit.editor.panes;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
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

public class TimelinePanel extends GridPane {
    private final String FONT_NAME = Font.getDefault().getName();

    public TimelinePanel() {
        add(getLayerPane(LayerType.VIDEO), 0, 0, 1, 1);
        add(getLayerPane(LayerType.AUDIO), 0, 1, 1, 1);

        double GRID_GAP = 6;

        setHgap(GRID_GAP);
        setVgap(GRID_GAP);
    }

    private VBox getLayerPane(LayerType type) {
        var mainBox = new VBox();
        var buttonBox = new HBox();

        var layerName = "";

        switch (type) {
            case AUDIO -> layerName = "Audio 1";
            case VIDEO -> layerName = "Video 1";
            case TEXT -> layerName = "Text 1";
        }

        var layerLabel = new Text(layerName);

        layerLabel.setTextAlignment(TextAlignment.LEFT);
        layerLabel.setFont(Font.font(FONT_NAME, FontWeight.BLACK, 16));

        var lockButton = new Button();
        lockButton.setCursor(Cursor.HAND);
        lockButton.setGraphic(FontIcon.of(FluentUiFilledMZ.UNLOCK_24));

        buttonBox.getChildren().add(lockButton);

        if (type == LayerType.VIDEO) {
            var enabledButton = new Button();
            enabledButton.setCursor(Cursor.HAND);
            enabledButton.setGraphic(FontIcon.of(FluentUiFilledAL.EYE_SHOW_24));

            buttonBox.getChildren().add(enabledButton);
        }

        if (type == LayerType.AUDIO) {
            var buttonFont = Font.font(FONT_NAME, FontWeight.BLACK, 13);
            var soloButton = new Button("S");
            soloButton.setCursor(Cursor.HAND);
            soloButton.setFont(buttonFont);
            soloButton.setTextFill(Color.DODGERBLUE);

            var muteButton = new Button("M");
            muteButton.setCursor(Cursor.HAND);
            muteButton.setFont(buttonFont);
            muteButton.setTextFill(Color.RED);

            var editButton = new Button();
            editButton.setCursor(Cursor.HAND);
            editButton.setGraphic(FontIcon.of(FluentUiFilledAL.EDIT_24));

            buttonBox.getChildren().add(editButton);
            buttonBox.getChildren().add(soloButton);
            buttonBox.getChildren().add(muteButton);
        }

        buttonBox.setSpacing(6);

        mainBox.getChildren().add(layerLabel);
        mainBox.getChildren().add(buttonBox);

        StyleManager.RegisterEvent((theme) -> {
            Color color = switch (theme) {
                case DARK -> Color.color(1, 1, 1, 0.05);
                case LIGHT -> Color.color(0, 0, 0, 0.05);
            };

            var background = new Background(new BackgroundFill(color, new CornerRadii(10), Insets.EMPTY));

            mainBox.setBackground(background);
        });

        mainBox.setSpacing(8);
        mainBox.setPadding(new Insets(8));
        mainBox.setMinWidth(280);

        return mainBox;
    }

    public enum LayerType {
        VIDEO,
        AUDIO,
        TEXT
    }
}
