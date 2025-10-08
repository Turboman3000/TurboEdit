package de.turboman.edit.editor.windows;

import de.turboman.edit.editor.panes.PreferencesPane;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static de.turboman.edit.editor.Editor.ICON;
import static de.turboman.edit.editor.Editor.TITLE;

public class PreferencesWindow extends Stage {

    public PreferencesWindow() {
        setTitle(TITLE + " - Preferences");
        getIcons().add(ICON);
        setAlwaysOnTop(true);

        final int WIDTH = 600;
        setMinWidth(WIDTH);
        setWidth(WIDTH);

        final int HEIGHT = 600;
        setMinHeight(HEIGHT);
        setHeight(HEIGHT);

        setScene(new Scene(new PreferencesPane()));

        initModality(Modality.APPLICATION_MODAL);
        showAndWait();
    }

}
