package turboedit.editor.panes.render_queue

import javafx.scene.control.TabPane
import turboedit.editor.misc.PreferencesFile

class RenderQueuePane : TabPane() {
    init {
        maxWidth = Double.Companion.MAX_VALUE
        maxHeight = Double.Companion.MAX_VALUE
        tabClosingPolicy = TabClosingPolicy.UNAVAILABLE

        for (server in PreferencesFile.CURRENT_PREFERENCES!!.renderServers) {
            tabs.add(ServerQueue(server))
        }
    }
}
