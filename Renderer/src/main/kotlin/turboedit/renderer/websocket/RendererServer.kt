package turboedit.renderer.websocket

import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import turboedit.renderer.Renderer.WEBSOCKET_PORT
import java.net.InetSocketAddress
import java.nio.ByteBuffer

class RendererServer : WebSocketServer(InetSocketAddress(WEBSOCKET_PORT)) {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun onOpen(ws: WebSocket?, clientHandshake: ClientHandshake?) {
    }

    override fun onClose(ws: WebSocket?, i: Int, s: String?, b: Boolean) {
    }

    override fun onMessage(webSocket: WebSocket?, s: String?) {
    }

    override fun onMessage(ws: WebSocket, buffer: ByteBuffer) {
        val id = buffer.get(0)
        val data = ByteArray(buffer.array().size - 1)

        for (x in 1..<data.size + 1) {
            data[x] = buffer.get(x)
        }

        when (id.toInt()) {
            0x00 -> { // Auth - Auth
                if (!WebsocketAuth.checkAuthPacket(data)) {
                    ws.close()
                }
            }

            0x01 -> {
                ws.send(WebsocketQueue.getQueueInfoData(data))
            }
        }
        println(data.contentToString())
    }

    override fun onError(ws: WebSocket?, e: Exception) {
        logger.error("{} = {}", e.javaClass.getName(), e.message)
    }

    override fun onStart() {
        logger.info("WebSocker Server running on port {}!", getPort())
    }
}
