package org.turbomedia.turboedit.editor;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.turbomedia.turboedit.editor.components.MenuBar;
import org.turbomedia.turboedit.editor.misc.Locale;
import org.turbomedia.turboedit.editor.misc.OsColorMode;
import org.turbomedia.turboedit.editor.misc.PreferencesFile;
import org.turbomedia.turboedit.editor.misc.StyleManager;
import org.turbomedia.turboedit.editor.panes.TimelinePanel;

import java.io.IOException;
import java.util.Objects;

import static org.turbomedia.turboedit.editor.misc.PreferencesFile.CURRENT_PREFERENCES;

public class Editor extends Application {
    public final static String TITLE = "TurboEdit Alpha 1";
    public final static AppDirs APP_DIRS = AppDirsFactory.getInstance();
    public final static String APPDATA = APP_DIRS.getUserDataDir("TurboEdit", null, "TurboMedia", true);
    public static Image ICON;

    private Logger logger = LoggerFactory.getLogger(getClass());

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

        var box = new VBox();

        box.setFillWidth(true);
        box.getChildren().add(new MenuBar(stage));

        var timeline = new TimelinePanel();

        timeline.setPadding(new Insets(15));

        box.getChildren().add(timeline);

        stage.setOnCloseRequest(event -> System.exit(0));

        stage.setScene(new Scene(box));

        stage.getIcons().add(ICON);
        stage.setTitle(TITLE);
        stage.setMaximized(true);
        stage.show();
    }
}
