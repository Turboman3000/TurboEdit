package turboedit.renderer.websocket

import com.google.protobuf.InvalidProtocolBufferException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.turbomedia.turboedit.shared.protocol.ProtocolAuth.AuthRequest
import turboedit.renderer.Envs

object WebsocketAuth {
    private val logger: Logger = LoggerFactory.getLogger(WebsocketAuth::class.java)

    fun checkAuthPacket(data: ByteArray?): Boolean {
        try {
            val packet = AuthRequest.parseFrom(data)

            return packet.getPassword().trim() == Envs.ACCESS_KEY!!.trim()
        } catch (e: InvalidProtocolBufferException) {
            logger.error("{} = {}", e.javaClass.getName(), e.message)
            return false
        }
    }
}
