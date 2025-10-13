package org.turbomedia.turboedit.editor.windows;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.turbomedia.turboedit.editor.panes.PreferencesPane;

import static org.turbomedia.turboedit.editor.Editor.ICON;
import static org.turbomedia.turboedit.editor.Editor.TITLE;
import static org.turbomedia.turboedit.editor.misc.Locale.GetText;

public class PreferencesWindow extends Stage {

    public PreferencesWindow() {
        setTitle(TITLE + " - " + GetText("title.preferences"));
        getIcons().add(ICON);

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
