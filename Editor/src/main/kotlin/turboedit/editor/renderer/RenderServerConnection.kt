package turboedit.editor.renderer

import turboedit.editor.events.EventSystem.callEvent
import turboedit.editor.events.EventType
import turboedit.editor.events.RendererServerStatusEventData

class RenderServerConnection(private val entry: RenderServerEntry, private var client: RendererClient?) {
    private var status: ConnectionStatus? = ConnectionStatus.CONNECTING

    fun entry(): RenderServerEntry {
        return entry
    }

    fun client(): RendererClient? {
        return client
    }

    fun client(client: RendererClient?) {
        this.client = client
    }

    fun status(): ConnectionStatus? {
        return status
    }

    @JvmOverloads
    fun status(status: ConnectionStatus, callEvent: Boolean = true) {
        if (callEvent) {
            val eventData = RendererServerStatusEventData(entry, status)
            callEvent(EventType.RENDERER_SERVER_STATUS, eventData)
        }

        if (this.status == ConnectionStatus.ERROR) return

        this.status = status
    }
}
