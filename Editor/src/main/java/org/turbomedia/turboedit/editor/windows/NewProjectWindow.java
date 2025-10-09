package org.turbomedia.turboedit.editor.windows;

import org.turbomedia.turboedit.editor.panes.NewProjectPane;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static org.turbomedia.turboedit.editor.Editor.ICON;
import static org.turbomedia.turboedit.editor.Editor.TITLE;
import static org.turbomedia.turboedit.editor.misc.Locale.GetText;

public class NewProjectWindow extends Stage {

    public NewProjectWindow(Stage stage) {
        getIcons().add(ICON);
        setTitle(TITLE + " - " + GetText("title.new_project"));
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
