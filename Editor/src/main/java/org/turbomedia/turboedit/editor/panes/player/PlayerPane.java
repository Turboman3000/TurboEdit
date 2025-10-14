package org.turbomedia.turboedit.editor.panes.player;

import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class PlayerPane extends VBox {

    public PlayerPane() {
        setSpacing(15);
        setMinWidth(1400);
        setMinHeight(800);
        setPadding(new Insets(15,15,0,15));

        try {
            getChildren().add(new PlayerPlayback());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        getChildren().add(new PlayerControls());
    }
}