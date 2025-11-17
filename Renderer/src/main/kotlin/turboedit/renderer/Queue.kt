package turboedit.renderer

import java.util.HashMap
import java.util.UUID

object Queue {
    private val queue = HashMap<String?, QueueItem?>()

    fun addToQueue(item: QueueItem?) {
        val id: String? = UUID.randomUUID().toString()
        queue.put(id, item)
    }

    fun getFromQueue(id: String?): QueueItem? {
        return queue.get(id)
    }

    fun removeFromQueue(id: String?) {
        queue.remove(id)
    }

    class QueueItem
}