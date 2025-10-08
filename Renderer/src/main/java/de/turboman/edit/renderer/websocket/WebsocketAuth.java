package de.turboman.edit.renderer.websocket;

import com.google.protobuf.InvalidProtocolBufferException;
import de.turboman.edit.renderer.Envs;
import de.turboman.edit.shared.protocol.ProtocolAuth;

public class WebsocketAuth {

    public static boolean CheckAuthPacket(byte[] data) {
        try {
            var packet = ProtocolAuth.AuthRequest.parseFrom(data);

            return packet.getPassword().strip().equals(Envs.TRENDER_PASSWORD.strip());
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }
}
