package turboedit.editor.panes.timeline

import javafx.geometry.Insets
import javafx.scene.layout.*

class TimelinePane : GridPane() {
    init {
        val gridGap = 6.0

        val column = ColumnConstraints()
        column.hgrow = Priority.ALWAYS

        val row = RowConstraints()
        row.vgrow = Priority.ALWAYS

        columnConstraints.add(column)
        rowConstraints.add(row)

        add(UpperBar(), 0, 0, 1, 1)

        val videoLayer1 = HBox(LayerBox(LayerBox.LayerType.VIDEO), ClipContainer(LayerBox.LayerType.VIDEO))
        videoLayer1.spacing = gridGap

        val audioLayer1 = HBox(LayerBox(LayerBox.LayerType.AUDIO), ClipContainer(LayerBox.LayerType.AUDIO))
        audioLayer1.spacing = gridGap

        add(videoLayer1, 0, 1, 1, 1)
        add(audioLayer1, 0, 2, 1, 1)

        hgap = gridGap
        vgap = gridGap
        padding = Insets(15.0)
    }
}
