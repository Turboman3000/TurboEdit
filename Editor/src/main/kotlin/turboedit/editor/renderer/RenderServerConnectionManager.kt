package turboedit.editor.renderer

object RenderServerConnectionManager {
    private val connections = HashMap<String?, RenderServerConnection?>()

    @JvmStatic
    fun establishConnection(entry: RenderServerEntry): RenderServerConnection? {
        if (connections.containsKey(entry.id())) {
            return connections.get(entry.id())
        }

        val connection = RenderServerConnection(entry, RendererClient(entry))

        connections.put(entry.id(), connection)

        return connection
    }

    fun getConnection(id: String?): RenderServerConnection? {
        return connections.get(id)
    }
}
