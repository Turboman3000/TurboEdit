package de.turboman.edit.editor.windows;

import de.turboman.edit.editor.panes.NewProjectPane;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static de.turboman.edit.editor.Editor.ICON;
import static de.turboman.edit.editor.Editor.TITLE;

public class NewProjectWindow extends Stage {

    public NewProjectWindow(Stage stage) {
        getIcons().add(ICON);
        setTitle(TITLE + " - New Project");
        setAlwaysOnTop(true);
        setResizable(false);

        int HEIGHT = 250;
        setMaxHeight(HEIGHT);
        setHeight(HEIGHT);

        int WIDTH = 450;
        setMaxWidth(WIDTH);
        setWidth(WIDTH);

        setScene(new Scene(new NewProjectPane(this, stage)));

        initModality(Modality.APPLICATION_MODAL);
        showAndWait();
    }
}
