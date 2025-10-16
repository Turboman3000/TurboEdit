package org.turbomedia.turboedit.editor.renderer;

import org.turbomedia.turboedit.editor.misc.PreferencesFile;

import static org.turbomedia.turboedit.editor.misc.Locale.GetText;

public final class RenderServerEntry {
    private final String id;
    private String displayName;
    private String ip;
    private FileResolveMethod fileResolveMethod;
    private boolean defaultServer;
    private final boolean buildIn;

    public RenderServerEntry(String id, String displayName, String ip, FileResolveMethod fileResolveMethod, boolean defaultServer, boolean buildIn) {
        this.id = id;
        this.displayName = displayName;
        this.ip = ip;
        this.fileResolveMethod = fileResolveMethod;
        this.defaultServer = defaultServer;
        this.buildIn = buildIn;
    }

    @Override
    public String toString() {
        return displayName + (PreferencesFile.CURRENT_PREFERENCES.showIPsForServers ? " [" + ip + "]" : "") + (buildIn ? " [" + GetText("render_server_entry.build_in") + "]" : "") + (defaultServer ? " [" + GetText("render_server_entry.default") + "]" : "");
    }

    public String id() {
        return id;
    }

    public String displayName() {
        return displayName;
    }

    public void displayName(String displayName) {
        this.displayName = displayName;
    }

    public String ip() {
        return ip;
    }

    public void ip(String ip) {
        this.ip = ip;
    }

    public FileResolveMethod fileResolveMethod() {
        return fileResolveMethod;
    }

    public void fileResolveMethod(FileResolveMethod fileResolveMethod) {
        this.fileResolveMethod = fileResolveMethod;
    }

    public boolean defaultServer() {
        return defaultServer;
    }

    public void defaultServer(boolean defaultServer) {
        this.defaultServer = defaultServer;
    }

    public boolean buildIn() {
        return buildIn;
    }
}