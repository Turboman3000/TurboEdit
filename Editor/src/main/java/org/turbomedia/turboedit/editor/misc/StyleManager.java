package org.turbomedia.turboedit.editor.misc;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import org.turbomedia.turboedit.editor.events.EventSystem;
import org.turbomedia.turboedit.editor.events.EventType;
import org.turbomedia.turboedit.editor.events.ThemeChangedEventData;

import static org.turbomedia.turboedit.editor.misc.PreferencesFile.CURRENT_PREFERENCES;

public class StyleManager {
    public static Theme CURRENT_THEME = Theme.DARK;

    public static void UpdateStyle() {
        var style = "";

        if (CURRENT_PREFERENCES.colorMode() == 0) {
            if (OsColorMode.isDarkMode()) {
                style = new PrimerDark().getUserAgentStylesheet();
                CURRENT_THEME = Theme.DARK;
            } else {
                style = new PrimerLight().getUserAgentStylesheet();
                CURRENT_THEME = Theme.LIGHT;
            }
        } else if (CURRENT_PREFERENCES.colorMode() == 1) {
            style = new PrimerDark().getUserAgentStylesheet();
            CURRENT_THEME = Theme.DARK;
        } else if (CURRENT_PREFERENCES.colorMode() == 2) {
            style = new PrimerLight().getUserAgentStylesheet();
            CURRENT_THEME = Theme.LIGHT;
        }

        Application.setUserAgentStylesheet(style);
        CallEvent();
    }

    public static void CallEvent() {
        EventSystem.CallEvent(EventType.THEME_CHANGED, new ThemeChangedEventData(CURRENT_THEME));
    }

    public enum Theme {
        DARK,
        LIGHT
    }
}
