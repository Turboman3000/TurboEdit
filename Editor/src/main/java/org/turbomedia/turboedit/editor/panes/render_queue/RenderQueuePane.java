package org.turbomedia.turboedit.editor.panes.render_queue;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class RenderQueuePane extends VBox {
    public RenderQueuePane() {
        var loadingText = new Text("Connecting to server...");
        var progressBar = new ProgressBar();

        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);

        getChildren().add(progressBar);
        getChildren().add(loadingText);

        VBox.setVgrow(this, Priority.ALWAYS);

        setAlignment(Pos.TOP_CENTER);

        setMaxWidth(Double.MAX_VALUE);
        setSpacing(6);
        setPadding(new Insets(15));
    }
}
