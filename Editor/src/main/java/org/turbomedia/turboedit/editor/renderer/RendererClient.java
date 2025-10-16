package org.turbomedia.turboedit.editor.renderer;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class RendererClient extends WebSocketClient {
    public static final int DEFAULT_PORT = 59992;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String serverName;

    public RendererClient(String server, String serverName) {
        super(URI.create("ws://" + server + "/"));
        this.serverName = serverName;

        connect();
        logger.info("Connecting to renderer: {} ({})", serverName, getURI());
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        logger.info("Connected to Renderer: {} ({})", serverName, getURI());
    }

    @Override
    public void onMessage(String s) {
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        logger.warn("Disconnected from renderer: {} ({})", serverName, getURI());
    }

    @Override
    public void onError(Exception e) {
        logger.error("An unexpected error occurred on RenderServer: {} ({})", serverName, getURI());
        logger.error(e.getMessage());
    }
}
