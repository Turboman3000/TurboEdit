package org.turbomedia.turboedit.editor.panes.render_queue;

import javafx.scene.control.TabPane;
import org.turbomedia.turboedit.editor.misc.PreferencesFile;

public class RenderQueuePane extends TabPane {
    public RenderQueuePane() {
        setMaxWidth(Double.MAX_VALUE);
        setMaxHeight(Double.MAX_VALUE);
        setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        for (var server : PreferencesFile.CURRENT_PREFERENCES.renderServers) {
            getTabs().add(new ServerQueue(server));
        }
    }
}
