package de.turboman.edit.renderer;

import de.turboman.edit.renderer.websocket.RendererServer;

public class Renderer {
    public static final int WEBSOCKET_PORT = 59992;
    public static volatile boolean isRunning = true;

    public static void main(String[] args) {
        new Thread(() -> {
            var server = new RendererServer();
            server.start();
        }).start();

        while (isRunning) {
            Thread.onSpinWait();
        }
    }
}
