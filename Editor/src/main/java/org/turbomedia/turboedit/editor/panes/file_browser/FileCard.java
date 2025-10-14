package org.turbomedia.turboedit.editor.panes.file_browser;

import javafx.geometry.Insets;
import javafx.scene.CacheHint;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import org.turbomedia.turboedit.editor.events.EventSystem;
import org.turbomedia.turboedit.editor.events.EventType;
import org.turbomedia.turboedit.editor.events.ThemeChangedEventData;

import java.io.IOException;

public class FileCard extends VBox {
    public FileCard() throws IOException {
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

        var resource = getClass().getResource("/test2.png");
        var stream = resource.openStream();
        var previewImage = new Image(stream);
        var preview = new ImageView(previewImage);

        preview.setPreserveRatio(true);
        preview.setFitWidth(160);
        preview.setCache(true);
        preview.setCacheHint(CacheHint.SPEED);

        var fileName = new Text("Test.mp4");
        fileName.setFontSmoothingType(FontSmoothingType.LCD);
        fileName.setFont(Font.font(FONT_NAME, 12));

        setCursor(Cursor.HAND);
        getChildren().add(preview);
        getChildren().add(fileName);
        setSpacing(6);
        setPadding(new Insets(6));
    }
}
