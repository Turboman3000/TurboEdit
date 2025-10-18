package org.turbomedia.turboedit.editor.panes.file_browser;

import javafx.geometry.Insets;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.turbomedia.turboedit.editor.events.EventSystem;
import org.turbomedia.turboedit.editor.events.EventType;
import org.turbomedia.turboedit.editor.events.ThemeChangedEventData;
import org.turbomedia.turboedit.editor.misc.StyleManager;
import org.turbomedia.turboedit.editor.windows.errors.FileErrorWindow;
import org.turbomedia.turboedit.shared.project.ProjectFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import static org.turbomedia.turboedit.editor.misc.Locale.GetText;

public class FileBrowserPane extends HBox {

    public FileBrowserPane() throws IOException {
        double GRID_GAP = 6;
        double width = 1050;
        double height = 800;

        var flow = new FlowPane();

        flow.setMinWidth(width);
        flow.setMinHeight(height);

        EventSystem.RegisterListener(EventType.THEME_CHANGED, (dat) -> {
            var data = (ThemeChangedEventData) dat;

            Color color = switch (data.theme()) {
                case DARK -> Color.color(1, 1, 1, 0.05);
                case LIGHT -> Color.color(0, 0, 0, 0.05);
            };

            var background = new Background(new BackgroundFill(color, new CornerRadii(10), Insets.EMPTY));

            flow.setBackground(background);
        });

        StyleManager.CallEvent();

        setOnDragOver((event) -> event.acceptTransferModes(TransferMode.LINK));
        setOnDragDropped((event) -> {
            var dragboard = event.getDragboard();
            var unsupportedFiles = new ArrayList<String>();

            for (var file : dragboard.getFiles()) {
                try {
                    var mimeType = Files.probeContentType(file.toPath());

                    if (!mimeType.startsWith("video/") && !mimeType.startsWith("audio/")) {
                        unsupportedFiles.add(file.getAbsolutePath());
                        continue;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                System.out.println(file.getAbsoluteFile());
            }

            if (!unsupportedFiles.isEmpty()) {
                new FileErrorWindow(
                        GetText("title.error.file.unsupported"),
                        GetText("error.file.unsupported"),
                        unsupportedFiles
                );
            }
        });

        flow.getChildren().add(new FileCard(new ProjectFile("Test.mp4", "", "video/mp4", null, "", 0, true, 1920, 1080, 60, "AV1", null)));
        flow.setPadding(new Insets(15));
        flow.setVgap(GRID_GAP);
        flow.setHgap(GRID_GAP);

        setPadding(new Insets(15));
        setSpacing(6);

        getChildren().add(new FileActions());
        getChildren().add(flow);
    }
}
