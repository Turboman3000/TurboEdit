package org.turbomedia.turboedit.editor.panes.preferences;

import javafx.scene.control.TabPane;

public class PreferencesPane extends TabPane {
    public PreferencesPane() {
        setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        getTabs().add(new GeneralTab());
        getTabs().add(new RenderingTab());
        getTabs().add(new PerformanceTab());
        getTabs().add(new SocialMediaTab());
    }
}
