package de.turboman.edit.renderer.websocket;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static de.turboman.edit.renderer.Renderer.WEBSOCKET_PORT;

public class RendererServer extends WebSocketServer {
    public RendererServer() {
        super(new InetSocketAddress(WEBSOCKET_PORT));
    }

    @Override
    public void onOpen(WebSocket ws, ClientHandshake clientHandshake) {
    }

    @Override
    public void onClose(WebSocket ws, int i, String s, boolean b) {
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
    }

    @Override
    public void onMessage(WebSocket ws, ByteBuffer buffer) {
        var id = buffer.get(0);
        var data = new byte[buffer.array().length - 1];

        for (var x = 1; x < data.length + 1; x++) {
            data[x] = buffer.get(x);
        }

        switch (id) {
            case 0x00 -> { // Auth - Auth
                if (!WebsocketAuth.CheckAuthPacket(data)) {
                    ws.close();
                }
            }
            case 0x01 -> {
                ws.send(WebsocketQueue.GetQueueInfoData(data));
            }
        }
        System.out.println(Arrays.toString(data));
    }

    @Override
    public void onError(WebSocket ws, Exception e) {
        System.err.println(e.getMessage());
    }

    @Override
    public void onStart() {
    }
}
