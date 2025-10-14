package org.turbomedia.turboedit.editor.panes.file_browser;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class FileCard extends VBox {
    public FileCard() throws IOException {
        var resource = getClass().getResource("/test2.png");
        var stream = resource.openStream();
        var previewImage = new Image(stream);
        var preview = new ImageView(previewImage);

        preview.setPreserveRatio(true);
        preview.setFitWidth(160);
;
        getChildren().add(preview);
        setSpacing(4);
    }
}
