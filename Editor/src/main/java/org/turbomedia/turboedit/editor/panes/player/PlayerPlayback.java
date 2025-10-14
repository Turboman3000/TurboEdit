package org.turbomedia.turboedit.editor.panes.player;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.io.IOException;

public class PlayerPlayback extends StackPane {
    public PlayerPlayback() throws IOException {
        var resource = getClass().getResource("/test2.png");
        var stream = resource.openStream();
        var previewImage = new Image(stream);
        var view = new ImageView(previewImage);

        view.setPreserveRatio(true);
        view.setCache(true);
        view.setCacheHint(CacheHint.SPEED);

        setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        setAlignment(Pos.CENTER);

        getChildren().add(view);
    }
}
