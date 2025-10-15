package org.turbomedia.turboedit.editor.windows;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.turbomedia.turboedit.editor.panes.render_queue.RenderQueuePane;

import static org.turbomedia.turboedit.editor.Editor.ICON;
import static org.turbomedia.turboedit.editor.Editor.TITLE;
import static org.turbomedia.turboedit.editor.misc.Locale.GetText;

public class RenderQueueWindow extends Stage {

    public RenderQueueWindow() {
        setTitle(TITLE + " - " + GetText("title.render_queue"));
        getIcons().add(ICON);
        setAlwaysOnTop(true);

        setWidth(500);
        setHeight(250);

        centerOnScreen();
        setScene(new Scene(new RenderQueuePane()));

        showAndWait();
    }

}
