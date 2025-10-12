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

import java.io.Console;

public class TimelinePanel extends GridPane {
    private final String fontName = Font.getDefault().getName();

    public TimelinePanel() {
        add(getLayerPane(), 0, 0, 1, 1);
    }

    private VBox getLayerPane() {
        var mainBox = new VBox();
        var buttonBox = new HBox();

        var layerName = new Text("Audio 1");

        layerName.setTextAlignment(TextAlignment.LEFT);
        layerName.setFont(Font.font(fontName, FontWeight.BLACK, 16));

        var lockButton = new Button();
        lockButton.setCursor(Cursor.HAND);
        lockButton.setGraphic(FontIcon.of(FluentUiFilledMZ.UNLOCK_24));

        var enabledButton = new Button();
        enabledButton.setCursor(Cursor.HAND);
        enabledButton.setGraphic(FontIcon.of(FluentUiFilledAL.EYE_SHOW_24));

        var buttonFont = Font.font(fontName, FontWeight.BLACK, 13);
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

        buttonBox.getChildren().add(lockButton);
        buttonBox.getChildren().add(enabledButton);
        buttonBox.getChildren().add(editButton);
        buttonBox.getChildren().add(soloButton);
        buttonBox.getChildren().add(muteButton);
        buttonBox.setSpacing(6);

        mainBox.getChildren().add(layerName);
        mainBox.getChildren().add(buttonBox);

        var background = new Background(new BackgroundFill(Color.color(1, 1, 1, 0.05), new CornerRadii(8), Insets.EMPTY));

        mainBox.setSpacing(8);
        mainBox.setBackground(background);
        mainBox.setPadding(new Insets(8));
        mainBox.setMinWidth(260);

        return mainBox;
    }
}
