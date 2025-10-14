package org.turbomedia.turboedit.editor.panes.player;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.fluentui.FluentUiFilledMZ;
import org.kordamp.ikonli.javafx.FontIcon;
import org.turbomedia.turboedit.editor.events.EventSystem;
import org.turbomedia.turboedit.editor.events.EventType;
import org.turbomedia.turboedit.editor.events.ThemeChangedEventData;
import org.turbomedia.turboedit.editor.misc.StyleManager;

public class PlayerControls extends HBox {
    public PlayerControls() {
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

        var previousButton = new Button();
        previousButton.setGraphic(new FontIcon(FluentUiFilledMZ.PREVIOUS_24));

        var playButton = new Button();
        playButton.setGraphic(new FontIcon(FluentUiFilledMZ.PLAY_24));

        var skipButton = new Button();
        skipButton.setGraphic(new FontIcon(FluentUiFilledMZ.NEXT_24));

        getChildren().add(previousButton);
        getChildren().add(playButton);
        getChildren().add(skipButton);

        setSpacing(6);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(6));
    }
}
