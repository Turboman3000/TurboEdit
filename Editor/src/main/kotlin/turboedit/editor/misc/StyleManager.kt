package turboedit.editor.misc

import atlantafx.base.theme.PrimerDark
import atlantafx.base.theme.PrimerLight
import javafx.application.Application
import turboedit.editor.events.EventSystem
import turboedit.editor.events.EventType
import turboedit.editor.events.ThemeChangedEventData
import turboedit.editor.misc.PreferencesFile.CURRENT_PREFERENCES

object StyleManager {
    @JvmField
    var CURRENT_THEME: Theme = Theme.DARK

    fun updateStyle() {
        var style = ""

        if (CURRENT_PREFERENCES!!.colorMode() == 0) {
            if (OsColorMode.isDarkMode()) {
                style = PrimerDark().userAgentStylesheet
                CURRENT_THEME = Theme.DARK
            } else {
                style = PrimerLight().userAgentStylesheet
                CURRENT_THEME = Theme.LIGHT
            }
        } else if (CURRENT_PREFERENCES!!.colorMode() == 1) {
            style = PrimerDark().userAgentStylesheet
            CURRENT_THEME = Theme.DARK
        } else if (CURRENT_PREFERENCES!!.colorMode() == 2) {
            style = PrimerLight().userAgentStylesheet
            CURRENT_THEME = Theme.LIGHT
        }

        Application.setUserAgentStylesheet(style)
        callEvent()
    }

    @JvmStatic
    fun callEvent() {
        EventSystem.callEvent(EventType.THEME_CHANGED, ThemeChangedEventData(CURRENT_THEME))
    }

    enum class Theme {
        DARK,
        LIGHT
    }
}
