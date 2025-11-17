package turboedit.editor.panes.preferences

import javafx.scene.control.TabPane

class PreferencesPane : TabPane() {
    init {
        tabClosingPolicy = TabClosingPolicy.UNAVAILABLE

        tabs.add(GeneralTab())
        tabs.add(RenderingTab())
        tabs.add(PerformanceTab())
        tabs.add(SocialMediaTab())
    }
}
