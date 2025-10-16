package org.turbomedia.turboedit.editor.windows.preferences;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.turbomedia.turboedit.editor.panes.preferences.EditRenderServerPane;
import org.turbomedia.turboedit.editor.renderer.FileResolveMethod;
import org.turbomedia.turboedit.editor.renderer.RenderServerEntry;

import static org.turbomedia.turboedit.editor.Editor.ICON;
import static org.turbomedia.turboedit.editor.Editor.TITLE;
import static org.turbomedia.turboedit.editor.misc.Locale.GetText;

public class AddRenderServerWindow extends Stage {
    public AddRenderServerWindow(String responseID) {
        setTitle(TITLE + " - " + GetText("title.add_render_server"));
        getIcons().add(ICON);
        setWidth(500);

        setScene(new Scene(new EditRenderServerPane(this, new RenderServerEntry("", "", FileResolveMethod.STREAMING, false, false), responseID)));

        setResizable(false);
        centerOnScreen();
        initModality(Modality.APPLICATION_MODAL);
        showAndWait();
    }
}
