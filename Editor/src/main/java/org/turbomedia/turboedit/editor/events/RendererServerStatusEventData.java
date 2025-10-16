package org.turbomedia.turboedit.editor.events;

import org.turbomedia.turboedit.editor.renderer.ConnectionStatus;
import org.turbomedia.turboedit.editor.renderer.RenderServerEntry;

public record RendererServerStatusEventData(RenderServerEntry entry, ConnectionStatus newStatus) {
}
