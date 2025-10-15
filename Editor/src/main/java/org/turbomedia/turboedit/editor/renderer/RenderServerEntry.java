package org.turbomedia.turboedit.editor.renderer;

import org.turbomedia.turboedit.editor.misc.PreferencesFile;

public record RenderServerEntry(String displayName, String ip, FileResolveMethod fileResolveMethod, boolean defaultServer, boolean buildIn) {
    @Override
    public String toString() {
        return displayName + (PreferencesFile.CURRENT_PREFERENCES.showIPsForServers ? " [" + ip + "]" : "") + (buildIn ? " [BUILD-IN]" : "") + (defaultServer ? " [DEFAULT]" : "");
    }
}