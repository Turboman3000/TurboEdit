package org.turbomedia.turboedit.editor.panes.timeline;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
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

public class UpperBar extends HBox {

    public UpperBar() {
        var FONT_NAME = Font.getDefault().getName();
        var timestamp = new Text("00:00:00.0000");

        timestamp.setFont(Font.font(FONT_NAME, FontWeight.BLACK, 24));
        timestamp.setTextAlignment(TextAlignment.CENTER);

        var newButton = new MenuButton();
        newButton.setCursor(Cursor.HAND);
        newButton.setGraphic(FontIcon.of(FluentUiFilledAL.ADD_SQUARE_24));
        {
            var audioItem = new MenuItem(GetText("timeline.upper_bar.add.audio"));
            audioItem.setGraphic(FontIcon.of(FluentUiFilledMZ.SPEAKER_24));

            newButton.getItems().add(audioItem);
        }

        var settingsButton = new MenuButton();
        settingsButton.setCursor(Cursor.HAND);
        settingsButton.setGraphic(FontIcon.of(FluentUiFilledMZ.SETTINGS_24));
        {
        }

        setSpacing(8);
        setPadding(new Insets(8));
        setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(this, Priority.ALWAYS);

        StyleManager.RegisterEvent((theme) -> {
            Color color = switch (theme) {
                case DARK -> Color.color(1, 1, 1, 0.05);
                case LIGHT -> Color.color(0, 0, 0, 0.05);
            };

            var background = new Background(new BackgroundFill(color, new CornerRadii(10), Insets.EMPTY));

            setBackground(background);
        });

        getChildren().add(timestamp);
        getChildren().add(newButton);
        getChildren().add(settingsButton);
    }
}
