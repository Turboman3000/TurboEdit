package turboedit.editor.renderer

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URI

class RendererClient(private val entry: RenderServerEntry) : WebSocketClient(URI.create("ws://" + entry.ip() + "/")) {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun connect() {
        super.connect()
        logger.info("Connecting to renderer: {} ({})", entry.displayName(), getURI())
    }

    init {
        connect()
    }

    override fun onOpen(serverHandshake: ServerHandshake?) {
        logger.info("Connected to Renderer: {} ({})", entry.displayName(), getURI())

        RenderServerConnectionManager.getConnection(entry.id())!!.status(ConnectionStatus.OPEN)
    }

    override fun onMessage(s: String?) {
    }

    override fun onClose(i: Int, s: String?, b: Boolean) {
        logger.warn("Disconnected from renderer: {} ({})", entry.displayName(), getURI())
        RenderServerConnectionManager.getConnection(entry.id())!!.status(ConnectionStatus.CLOSED)
    }

    override fun onError(e: Exception) {
        logger.error("An unexpected error occurred on RenderServer: {} ({})", entry.displayName(), getURI())

        e.printStackTrace()
        RenderServerConnectionManager.getConnection(entry.id())!!.status(ConnectionStatus.ERROR)
    }

    companion object {
        const val DEFAULT_PORT: Int = 59992
    }
}
