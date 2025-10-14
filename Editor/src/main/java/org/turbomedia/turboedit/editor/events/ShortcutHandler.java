package org.turbomedia.turboedit.editor.events;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.turbomedia.turboedit.editor.windows.PreferencesWindow;
import org.turbomedia.turboedit.editor.windows.RenderQueueWindow;

public class ShortcutHandler {
    public static void RegisterGlobalShortcuts(Scene scene) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, (event) -> {
            if (event.isControlDown()) {
                if (event.getCode() == KeyCode.Q) {
                    new RenderQueueWindow();
                } else if (event.getCode() == KeyCode.P) {
                    new PreferencesWindow();
                }
            } else {
                if (event.getCode() == KeyCode.SPACE) {
                    EventSystem.CallEvent(EventType.PLAYBACK_TOGGLE, null);
                }
            }
        });
    }
}
