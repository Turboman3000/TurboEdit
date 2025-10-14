package org.turbomedia.turboedit.editor.panes.file_browser;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.turbomedia.turboedit.editor.events.EventSystem;
import org.turbomedia.turboedit.editor.events.EventType;
import org.turbomedia.turboedit.editor.events.ThemeChangedEventData;
import org.turbomedia.turboedit.editor.misc.StyleManager;

import java.io.IOException;

public class FileBrowserPane extends GridPane {

    public FileBrowserPane() throws IOException {
        double GRID_GAP = 6;

        var flow = new FlowPane();

        EventSystem.RegisterListener(EventType.THEME_CHANGED, (dat) -> {
            var data = (ThemeChangedEventData) dat;

            Color color = switch (data.theme()) {
                case DARK -> Color.color(1, 1, 1, 0.05);
                case LIGHT -> Color.color(0, 0, 0, 0.05);
            };

            var background = new Background(new BackgroundFill(color, new CornerRadii(10), Insets.EMPTY));

            flow.setBackground(background);
        });

        StyleManager.CallEvent();

        flow.getChildren().add(new FileCard());
        flow.setMinWidth(1000);
        flow.setMinHeight(800);
        flow.setPadding(new Insets(15));
        flow.setVgap(GRID_GAP);
        flow.setHgap(GRID_GAP);

        setPadding(new Insets(15,15,0,15));

        getChildren().add(flow);
    }
}
