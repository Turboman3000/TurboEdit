package turboedit.editor.events

import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import turboedit.editor.windows.RenderQueueWindow
import turboedit.editor.windows.preferences.PreferencesWindow

object ShortcutHandler {
    fun registerGlobalShortcuts(scene: Scene) {
        scene.addEventFilter<KeyEvent?>(KeyEvent.KEY_PRESSED, EventHandler { event: KeyEvent ->
            if (event.isControlDown) {
                if (event.code == KeyCode.Q) {
                    RenderQueueWindow()
                } else if (event.code == KeyCode.P) {
                    PreferencesWindow()
                }
            } else {
                if (event.code == KeyCode.SPACE) {
                    EventSystem.callEvent(EventType.PLAYBACK_TOGGLE)
                }
            }
        })
    }
}
