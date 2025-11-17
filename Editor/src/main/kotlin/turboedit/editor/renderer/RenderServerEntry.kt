package turboedit.editor.renderer

import turboedit.editor.misc.Locale
import turboedit.editor.misc.PreferencesFile

class RenderServerEntry(
    private val id: String?,
    private var displayName: String?,
    private var ip: String,
    private var fileResolveMethod: FileResolveMethod?,
    private var defaultServer: Boolean,
    private val buildIn: Boolean
) {
    override fun toString(): String {
        return displayName + (if (PreferencesFile.CURRENT_PREFERENCES!!.showIPsForServers) " [$ip]" else "") + (if (buildIn) " [" + Locale.getText(
            "render_server_entry.build_in"
        ) + "]" else "") + (if (defaultServer) " [" + Locale.getText("render_server_entry.default") + "]" else "")
    }

    fun id(): String? {
        return id
    }

    fun displayName(): String? {
        return displayName
    }

    fun displayName(displayName: String?) {
        this.displayName = displayName
    }

    fun ip(): String {
        return ip
    }

    fun ip(ip: String) {
        this.ip = ip
    }

    fun fileResolveMethod(): FileResolveMethod? {
        return fileResolveMethod
    }

    fun fileResolveMethod(fileResolveMethod: FileResolveMethod?) {
        this.fileResolveMethod = fileResolveMethod
    }

    fun defaultServer(): Boolean {
        return defaultServer
    }

    fun defaultServer(defaultServer: Boolean) {
        this.defaultServer = defaultServer
    }

    fun buildIn(): Boolean {
        return buildIn
    }
}