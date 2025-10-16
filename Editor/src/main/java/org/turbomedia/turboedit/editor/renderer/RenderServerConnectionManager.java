package org.turbomedia.turboedit.editor.renderer;

import java.util.HashMap;

public class RenderServerConnectionManager {
    private static final HashMap<String, RenderServerConnection> connections = new HashMap<>();

    public static RenderServerConnection EstablishConnection(RenderServerEntry entry) {
        if (connections.containsKey(entry.id())) {
            return connections.get(entry.id());
        }

        var connection = new RenderServerConnection(entry, new RendererClient(entry));

        connections.put(entry.id(), connection);

        return connection;
    }

    public static RenderServerConnection getConnection(String id) {
        return connections.get(id);
    }
}
