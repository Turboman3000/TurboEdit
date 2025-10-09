package org.turbomedia.turboedit.renderer.websocket;

import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.turbomedia.turboedit.renderer.Envs;
import org.turbomedia.turboedit.shared.protocol.ProtocolAuth;

public class WebsocketAuth {
    private static final Logger logger = LoggerFactory.getLogger(WebsocketAuth.class);

    public static boolean CheckAuthPacket(byte[] data) {
        try {
            var packet = ProtocolAuth.AuthRequest.parseFrom(data);

            return packet.getPassword().strip().equals(Envs.ACCESS_KEY.strip());
        } catch (InvalidProtocolBufferException e) {
            logger.error("{} = {}", e.getClass().getName(), e.getMessage());
            return false;
        }
    }
}
