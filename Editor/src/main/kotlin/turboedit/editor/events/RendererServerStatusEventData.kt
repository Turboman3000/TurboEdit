package turboedit.editor.events

import turboedit.editor.renderer.ConnectionStatus
import turboedit.editor.renderer.RenderServerEntry

data class RendererServerStatusEventData(val entry: RenderServerEntry,val newStatus: ConnectionStatus)
