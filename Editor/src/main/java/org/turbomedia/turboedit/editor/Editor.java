package org.turbomedia.turboedit.editor;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.turbomedia.turboedit.editor.components.MenuBar;
import org.turbomedia.turboedit.editor.events.EventSystem;
import org.turbomedia.turboedit.editor.events.EventType;
import org.turbomedia.turboedit.editor.events.WindowResizeEventData;
import org.turbomedia.turboedit.editor.misc.Locale;
import org.turbomedia.turboedit.editor.misc.PreferencesFile;
import org.turbomedia.turboedit.editor.misc.StyleManager;
import org.turbomedia.turboedit.editor.panes.file_browser.FileBrowserPane;
import org.turbomedia.turboedit.editor.panes.player.PlayerPane;
import org.turbomedia.turboedit.editor.panes.timeline.TimelinePane;

import java.io.IOException;
import java.util.Objects;

public class Editor extends Application {
    public final static String TITLE = "TurboEdit Alpha 1";
    public final static AppDirs APP_DIRS = AppDirsFactory.getInstance();
    public final static String APPDATA = APP_DIRS.getUserDataDir("TurboEdit", null, "TurboMedia", true);
    public static Image ICON;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        ICON = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icon.png")));

        try {
            PreferencesFile.Read();
        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage());
        }

        StyleManager.UpdateStyle();

        try {
            new Locale().LoadLocales();
        } catch (IOException e) {
            logger.error("{} = {}", e.getClass().getName(), e.getMessage());
        }

        var column = new ColumnConstraints();
        column.setHgrow(Priority.ALWAYS);

        var grid = new GridPane();
        grid.getColumnConstraints().add(column);

        grid.add(new MenuBar(stage), 0, 0, 2, 1);

        try {
            grid.add(new FileBrowserPane(), 0, 1, 1, 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        grid.add(new PlayerPane(), 1, 1, 1, 1);
        grid.add(new TimelinePane(), 0, 2, 2, 1);

        stage.setOnCloseRequest(event -> System.exit(0));

        stage.setScene(new Scene(grid));

        stage.setMinWidth(640);
        stage.setMinHeight(480);
        stage.setMaxHeight(Double.MAX_VALUE);
        stage.setMaxWidth(Double.MAX_VALUE);

        stage.getIcons().add(ICON);
        stage.setTitle(TITLE);
        stage.setMaximized(true);
        stage.show();

        stage.widthProperty().addListener(((obs, oldValue, newValue) -> {
            var data = new WindowResizeEventData(
                    newValue.intValue(),
                    (int) stage.getHeight(),
                    newValue.doubleValue() / 1920,
                    stage.getHeight() / 1080
            );

            EventSystem.CallEvent(EventType.WINDOW_RESIZE, data);
        }));

        stage.heightProperty().addListener(((obs, oldValue, newValue) -> {
            var data = new WindowResizeEventData(
                    (int) stage.getWidth(),
                    newValue.intValue(),
                    stage.getWidth() / 1920,
                    newValue.doubleValue() / 1080
            );

            EventSystem.CallEvent(EventType.WINDOW_RESIZE, data);
        }));

        var data = new WindowResizeEventData(
                (int) stage.getWidth(),
                (int) stage.getHeight(),
                stage.getWidth() / 1920,
                stage.getHeight() / 1080
        );

        EventSystem.CallEvent(EventType.WINDOW_RESIZE, data);
    }
}
