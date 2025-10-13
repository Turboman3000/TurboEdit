package org.turbomedia.turboedit.editor.panes.timeline;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.turbomedia.turboedit.editor.events.EventSystem;
import org.turbomedia.turboedit.editor.events.EventType;
import org.turbomedia.turboedit.editor.events.ThemeChangedEventData;
import org.turbomedia.turboedit.editor.misc.StyleManager;

import java.io.IOException;

public class ClipContainer extends HBox {

    public ClipContainer(LayerBox.LayerType type) {
        setSpacing(4);
        setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(this, Priority.ALWAYS);

        EventSystem.RegisterListener(EventType.THEME_CHANGED, (dat) -> {
            var data = (ThemeChangedEventData) dat;

            Color color = switch (data.theme()) {
                case DARK -> Color.color(1, 1, 1, 0.05);
                case LIGHT -> Color.color(0, 0, 0, 0.05);
            };

            var background = new Background(new BackgroundFill(color, new CornerRadii(10), Insets.EMPTY));

            setBackground(background);

            getChildren().clear();

            try {
                getChildren().add(new ClipCard(type));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        StyleManager.CallEvent();
    }
}
