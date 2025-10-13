package org.turbomedia.turboedit.editor.events;

import org.turbomedia.turboedit.editor.misc.StyleManager;

public record ThemeChangedEventData(StyleManager.Theme theme) {
}
