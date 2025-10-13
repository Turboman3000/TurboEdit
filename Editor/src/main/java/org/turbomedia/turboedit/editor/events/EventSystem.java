package org.turbomedia.turboedit.editor.events;

import java.util.HashMap;
import java.util.UUID;

public class EventSystem {

    private static final HashMap<String, Event> listeners = new HashMap<>();

    public static String RegisterListener(EventType type, EventAction function) {
        var id = UUID.randomUUID().toString();

        listeners.put(id, new Event(type, function));

        return id;
    }

    public static void RemoveListener(String id) {
        listeners.remove(id);
    }

    public static void CallEvent(EventType type, Object data) {
        var map = (HashMap<String, Event>) listeners.clone();

        for (var event : map.values()) {
            if (event.type == type) {
                event.action.onEvent(data);
            }
        }
    }

    public record Event(EventType type, EventAction action) {
    }

    public interface EventAction {
        void onEvent(Object data);
    }
}
