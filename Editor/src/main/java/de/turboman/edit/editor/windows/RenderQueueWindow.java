package de.turboman.edit.editor.windows;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import static de.turboman.edit.editor.Editor.ICON;
import static de.turboman.edit.editor.Editor.TITLE;

public class RenderQueueWindow extends Stage {

    public RenderQueueWindow() {
        setTitle(TITLE + " - Render Queue");
        getIcons().add(ICON);
        setAlwaysOnTop(true);

        setWidth(500);
        setHeight(250);

        setScene(new Scene(new GridPane()));

        show();
    }

}
