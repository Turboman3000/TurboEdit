package org.turbomedia.turboedit.editor.windows.preferences;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.turbomedia.turboedit.editor.panes.preferences.PreferencesPane;

import static org.turbomedia.turboedit.editor.Editor.ICON;
import static org.turbomedia.turboedit.editor.Editor.TITLE;
import static org.turbomedia.turboedit.editor.misc.Locale.GetText;

public class AddServerWindow extends Stage {
    public AddServerWindow() {
        setTitle(TITLE + " - " + GetText("title.preferences"));
        getIcons().add(ICON);

        final int WIDTH = 500;
        setMinWidth(WIDTH);
        setWidth(WIDTH);

        final int HEIGHT = 400;
        setMinHeight(HEIGHT);
        setHeight(HEIGHT);

        setScene(new Scene(new GridPane()));

        centerOnScreen();
        initModality(Modality.APPLICATION_MODAL);
        showAndWait();
    }
}
