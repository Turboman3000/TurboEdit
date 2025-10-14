package org.turbomedia.turboedit.editor.panes.player;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.turbomedia.turboedit.editor.events.EventSystem;
import org.turbomedia.turboedit.editor.events.EventType;
import org.turbomedia.turboedit.editor.events.ThemeChangedEventData;
import org.turbomedia.turboedit.editor.events.WindowResizeEventData;
import org.turbomedia.turboedit.editor.misc.StyleManager;

import java.io.IOException;

public class PlayerPlayback extends GridPane {
    public PlayerPlayback() throws IOException {
        double borderRadius = 10.0;
        double padding = 12;

        var resource = getClass().getResource("/test2.png");
        var stream = resource.openStream();
        var previewImage = new Image(stream);
        var view = new ImageView(previewImage);

        view.setPreserveRatio(true);
        view.setCache(true);
        view.setCacheHint(CacheHint.SPEED);

        var clip = new Rectangle(view.getFitWidth(), view.getFitHeight());

        clip.setArcWidth(borderRadius * 2);
        clip.setArcHeight(borderRadius * 2);

        EventSystem.RegisterListener(EventType.WINDOW_RESIZE, (dat) -> {
            var data = (WindowResizeEventData) dat;

            view.setFitHeight(555 * data.heightScale() - (padding * 2));

            clip.setWidth(view.layoutBoundsProperty().get().getWidth());
            clip.setHeight(view.layoutBoundsProperty().get().getHeight());
        });

        view.setClip(clip);
        EventSystem.RegisterListener(EventType.THEME_CHANGED, (dat) -> {
            var data = (ThemeChangedEventData) dat;

            Color color = switch (data.theme()) {
                case DARK -> Color.color(1, 1, 1, 0.05);
                case LIGHT -> Color.color(0, 0, 0, 0.05);
            };

            var background = new Background(new BackgroundFill(color, new CornerRadii(borderRadius), Insets.EMPTY));

            setBackground(background);
        });

        StyleManager.CallEvent();

        setAlignment(Pos.CENTER);
        setPadding(new Insets(padding));
        add(view, 0, 0, 1, 1);
    }
}
