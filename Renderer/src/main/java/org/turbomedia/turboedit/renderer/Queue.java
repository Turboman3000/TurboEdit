package org.turbomedia.turboedit.renderer;

import java.util.HashMap;
import java.util.UUID;

public class Queue {
    private static HashMap<String, QueueItem> queue = new HashMap<>();

    public static void AddToQueue(QueueItem item) {
        var id = UUID.randomUUID().toString();
        queue.put(id, item);
    }

    public static QueueItem GetFromQueue(String id) {
        return queue.get(id);
    }

    public static void RemoveFromQueue(String id) {
        queue.remove(id);
    }

    public record QueueItem(){}
}
