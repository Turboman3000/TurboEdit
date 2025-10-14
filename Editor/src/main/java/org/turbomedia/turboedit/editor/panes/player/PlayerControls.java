package org.turbomedia.turboedit.editor.panes.player;

import atlantafx.base.controls.Spacer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.kordamp.ikonli.fluentui.FluentUiFilledMZ;
import org.kordamp.ikonli.javafx.FontIcon;
import org.turbomedia.turboedit.editor.events.EventSystem;
import org.turbomedia.turboedit.editor.events.EventType;
import org.turbomedia.turboedit.editor.events.ThemeChangedEventData;
import org.turbomedia.turboedit.editor.misc.StyleManager;

public class PlayerControls extends VBox {
    public PlayerControls() {
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

        StyleManager.CallEvent();

        var slider = new Slider();
        slider.setCursor(Cursor.OPEN_HAND);

        slider.setOnMousePressed((event) -> slider.setCursor(Cursor.CLOSED_HAND));
        slider.setOnMouseReleased((event) -> slider.setCursor(Cursor.OPEN_HAND));

        var timestampFont = Font.font(FONT_NAME, FontWeight.BOLD, 20);

        var currentTimestamp = new Text("00:00:00");
        currentTimestamp.setFont(timestampFont);

        var totalTimestamp = new Text("00:42:00");
        totalTimestamp.setFont(timestampFont);

        var previousButton = new Button();
        previousButton.setGraphic(new FontIcon(FluentUiFilledMZ.PREVIOUS_24));
        previousButton.setCursor(Cursor.HAND);

        var playButton = new Button();
        playButton.setGraphic(new FontIcon(FluentUiFilledMZ.PLAY_24));
        playButton.setCursor(Cursor.HAND);

        var skipButton = new Button();
        skipButton.setGraphic(new FontIcon(FluentUiFilledMZ.NEXT_24));
        skipButton.setCursor(Cursor.HAND);

        var hbox = new HBox(currentTimestamp, new Spacer(), previousButton, playButton, skipButton, new Spacer(), totalTimestamp);

        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(6);

        setPadding(new Insets(6));

        setSpacing(6);
        getChildren().add(slider);
        getChildren().add(hbox);
    }
}
