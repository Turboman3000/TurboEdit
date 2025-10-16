package org.turbomedia.turboedit.editor.panes.render_queue;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.turbomedia.turboedit.editor.events.EventSystem;
import org.turbomedia.turboedit.editor.events.EventType;
import org.turbomedia.turboedit.editor.events.RendererServerStatusEventData;
import org.turbomedia.turboedit.editor.renderer.ConnectionStatus;
import org.turbomedia.turboedit.editor.renderer.RenderServerConnection;
import org.turbomedia.turboedit.editor.renderer.RenderServerConnectionManager;
import org.turbomedia.turboedit.editor.renderer.RenderServerEntry;

public class ServerQueue extends Tab {
    private final RenderServerConnection connection;

    private volatile boolean isConnected;

    public ServerQueue(RenderServerEntry entry) {
        setText(entry.displayName());

        var box = new VBox();
        connection = RenderServerConnectionManager.EstablishConnection(entry);

        VBox.setVgrow(box, Priority.ALWAYS);
        box.setPadding(new Insets(15));
        box.setAlignment(Pos.TOP_CENTER);

        box.getChildren().add(getLoading());

        if (connection.status() == ConnectionStatus.OPEN) {
            box.getChildren().clear();
            isConnected = true;
        }

        EventSystem.RegisterListener(EventType.RENDERER_SERVER_STATUS, (dat) -> {
            var data = ((RendererServerStatusEventData) dat);

            if (!data.entry().id().equals(entry.id())) return;
            if (data.newStatus() == ConnectionStatus.OPEN) {
                isConnected = true;
            }
        });

        setContent(box);

        Platform.runLater(() -> {
            while (!isConnected) {
                Thread.onSpinWait();
            }

            box.getChildren().clear();
        });
    }

    private VBox getLoading() {
        var loadingText = new Text("Connecting to server...");
        var ipText = new Text(connection.entry().ip());
        var box = new VBox(loadingText, ipText);

        VBox.setVgrow(box, Priority.ALWAYS);
        box.setAlignment(Pos.TOP_CENTER);
        box.setSpacing(6);

        return box;
    }
}
