package turboedit.editor.panes.render_queue

import javafx.application.Platform
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Tab
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import turboedit.editor.events.EventSystem.EventAction
import turboedit.editor.events.EventSystem.registerListener
import turboedit.editor.events.EventType
import turboedit.editor.events.RendererServerStatusEventData
import turboedit.editor.renderer.ConnectionStatus
import turboedit.editor.renderer.RenderServerConnection
import turboedit.editor.renderer.RenderServerConnectionManager.establishConnection
import turboedit.editor.renderer.RenderServerEntry
import kotlin.concurrent.Volatile

class ServerQueue(entry: RenderServerEntry) : Tab() {
    private val connection: RenderServerConnection?

    @Volatile
    private var isConnected = false

    init {
        text = entry.displayName()

        val box = VBox()
        connection = establishConnection(entry)

        VBox.setVgrow(box, Priority.ALWAYS)
        box.padding = Insets(15.0)
        box.alignment = Pos.TOP_CENTER

        box.children.add(this.loading)

        if (connection!!.status() == ConnectionStatus.OPEN) {
            box.children.clear()
            isConnected = true
        }

        registerListener(EventType.RENDERER_SERVER_STATUS, EventAction { dat: Any? ->
            val data = (dat as RendererServerStatusEventData?)

            if (data!!.entry.id() != entry.id()) {
                return@EventAction
            }

            if (data.newStatus == ConnectionStatus.OPEN) {
                isConnected = true
            }
        })

        content = box

        Platform.runLater(Runnable {
            while (!isConnected) {
                Thread.onSpinWait()
            }

            box.children.clear()
        })
    }

    private val loading: VBox
        get() {
            val loadingText = Text("Connecting to server...")
            val ipText = Text(connection!!.entry().ip())
            val box = VBox(loadingText, ipText)

            VBox.setVgrow(box, Priority.ALWAYS)
            box.alignment = Pos.TOP_CENTER
            box.spacing = 6.0

            return box
        }
}
