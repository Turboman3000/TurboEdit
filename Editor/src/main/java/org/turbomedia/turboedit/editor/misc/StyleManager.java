package org.turbomedia.turboedit.editor.misc;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;

import java.util.HashMap;

import static org.turbomedia.turboedit.editor.misc.PreferencesFile.CURRENT_PREFERENCES;

public class StyleManager {
    public static Theme CURRENT_THEME = Theme.DARK;

    private static final HashMap<Integer, EventFunction> listeners = new HashMap<>();

    public static void ClearAllEvents() {
        listeners.clear();
    }

    public static int RegisterEvent(EventFunction onEvent) {
        listeners.put(listeners.size(), onEvent);

        onEvent.onEvent(CURRENT_THEME);

        return listeners.size() - 1;
    }

    public static void RemoveEvent(int id) {
        listeners.remove(id);
    }

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

        for (var listener : listeners.values()) {
            listener.onEvent(CURRENT_THEME);
        }
    }

    public interface EventFunction {
        void onEvent(Theme theme);
    }

    public enum Theme {
        DARK,
        LIGHT
    }
}
