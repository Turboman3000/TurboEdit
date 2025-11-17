package turboedit.editor.events

import java.util.*

object EventSystem {
    private val listeners = HashMap<String?, Event?>()

    @JvmStatic
    fun registerListener(type: EventType, function: EventAction): String {
        val id: String = UUID.randomUUID().toString()

        listeners.put(id, Event(type, function))

        return id
    }

    fun removeListener(id: String?) {
        listeners.remove(id)
    }

    @JvmStatic
    fun callEvent(type: EventType, data: Any? = null) {
        val map = listeners.clone() as HashMap<String, Event>

        for (event in map.values) {
            if (event.type == type) {
                event.action.onEvent(data)
            }
        }
    }

    @JvmRecord
    data class Event(val type: EventType, val action: EventAction)

    fun interface EventAction {
        fun onEvent(data: Any?)
    }
}
