package org.turbomedia.turboedit.editor.panes.timeline;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class TimelinePanel extends GridPane {

    public TimelinePanel() {
        double GRID_GAP = 6;

        var column = new ColumnConstraints();
        column.setHgrow(Priority.ALWAYS);

        getColumnConstraints().add(column);

        add(new UpperBar(), 0, 0, 1, 1);

        var videoLayer1 = new HBox(new LayerBox(LayerBox.LayerType.VIDEO), new ClipContainer(LayerBox.LayerType.VIDEO));
        videoLayer1.setSpacing(GRID_GAP);

        var audioLayer1 = new HBox(new LayerBox(LayerBox.LayerType.AUDIO), new ClipContainer(LayerBox.LayerType.AUDIO));
        audioLayer1.setSpacing(GRID_GAP);

        add(videoLayer1, 0, 1, 1, 1);
        add(audioLayer1, 0, 2, 1, 1);

        setHgap(GRID_GAP);
        setVgap(GRID_GAP);
    }
}
