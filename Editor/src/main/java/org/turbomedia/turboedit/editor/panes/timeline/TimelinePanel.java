package org.turbomedia.turboedit.editor.panes.timeline;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class TimelinePanel extends GridPane {

    public TimelinePanel() {
        var column = new ColumnConstraints();
        column.setHgrow(Priority.ALWAYS);

        getColumnConstraints().add(column);

        add(new UpperBar(), 0, 0, 1, 1);
        add(new LayerBox(LayerBox.LayerType.VIDEO), 0, 1, 1, 1);
        add(new LayerBox(LayerBox.LayerType.AUDIO), 0, 2, 1, 1);

        double GRID_GAP = 6;

        setHgap(GRID_GAP);
        setVgap(GRID_GAP);
    }
}
