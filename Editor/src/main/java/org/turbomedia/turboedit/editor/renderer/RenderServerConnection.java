package org.turbomedia.turboedit.editor.renderer;

import org.turbomedia.turboedit.editor.events.EventSystem;
import org.turbomedia.turboedit.editor.events.EventType;
import org.turbomedia.turboedit.editor.events.RendererServerStatusEventData;

public final class RenderServerConnection {
    private final RenderServerEntry entry;
    private RendererClient client;
    private ConnectionStatus status = ConnectionStatus.CONNECTING;

    public RenderServerConnection(RenderServerEntry entry, RendererClient client) {
        this.entry = entry;
        this.client = client;
    }

    public RenderServerEntry entry() {
        return entry;
    }

    public RendererClient client() {
        return client;
    }

    public void client(RendererClient client) {
        this.client = client;
    }

    public ConnectionStatus status() {
        return status;
    }

    public void status(ConnectionStatus status) {
        status(status, true);
    }

    public void status(ConnectionStatus status, boolean callEvent) {
        if (callEvent) {
            var eventData = new RendererServerStatusEventData(entry, status);
            EventSystem.CallEvent(EventType.RENDERER_SERVER_STATUS, eventData);
        }

        if (this.status == ConnectionStatus.ERROR) return;
        this.status = status;
    }
}
