package org.turbomedia.turboedit.renderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.turbomedia.turboedit.renderer.websocket.RendererServer;

public class Renderer {
    public static final int WEBSOCKET_PORT = 59992;
    public static volatile boolean isRunning = true;

    private static final Logger logger = LoggerFactory.getLogger(Renderer.class);

    public static void main(String[] args) {
        logger.info("Loading Rendering Server...");

        new Thread(() -> {
            var server = new RendererServer();
            server.start();
        }).start();

        while (isRunning) {
            Thread.onSpinWait();
        }
    }
}
