package org.turbomedia.turboedit.editor.renderer;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class RendererClient extends WebSocketClient {
    public static final int DEFAULT_PORT = 59992;

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final RenderServerEntry entry;

    @Override
    public void connect() {
        super.connect();
        logger.info("Connecting to renderer: {} ({})", entry.displayName(), getURI());
    }

    public RendererClient(RenderServerEntry entry) {
        super(URI.create("ws://" + entry.ip() + "/"));
        this.entry = entry;

        connect();
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        logger.info("Connected to Renderer: {} ({})", entry.displayName(), getURI());

        RenderServerConnectionManager.getConnection(entry.id()).status(ConnectionStatus.OPEN);
    }

    @Override
    public void onMessage(String s) {
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        logger.warn("Disconnected from renderer: {} ({})", entry.displayName(), getURI());
        RenderServerConnectionManager.getConnection(entry.id()).status(ConnectionStatus.CLOSED);
    }

    @Override
    public void onError(Exception e) {
        logger.error("An unexpected error occurred on RenderServer: {} ({})", entry.displayName(), getURI());

        e.printStackTrace();
        RenderServerConnectionManager.getConnection(entry.id()).status(ConnectionStatus.ERROR);
    }
}
