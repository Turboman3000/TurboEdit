package de.turboman.edit.editor;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import de.turboman.edit.editor.components.MenuBar;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;

import java.io.IOException;
import java.util.Objects;

import static de.turboman.edit.editor.PreferencesFile.CURRENT_PREFERENCES;

public class Editor extends Application {
    public final static String TITLE = "TurboEdit Alpha 1";
    public final static AppDirs APP_DIRS = AppDirsFactory.getInstance();
    public final static String APPDATA = APP_DIRS.getUserDataDir("TurboEdit", null, "TurboMedia", true);
    public static Image ICON;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        ICON = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icon.png")));

        try {
            PreferencesFile.Read();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        SetColorMode();

        var box = new VBox();

        box.setFillWidth(true);
        box.getChildren().add(new MenuBar(stage));
        // box.getChildren().add(new TestPanel());

        stage.setOnCloseRequest(event -> System.exit(0));

        stage.setScene(new Scene(box));

        stage.getIcons().add(ICON);
        stage.setTitle(TITLE);
        stage.setMaximized(true);
        stage.show();
    }

    public static void SetColorMode() {
        if (CURRENT_PREFERENCES.colorMode() == 0) {
            if (OSColorMode.isDarkMode()) {
                Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
            } else {
                Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
            }
        } else if (CURRENT_PREFERENCES.colorMode() == 1) {
            Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        } else if (CURRENT_PREFERENCES.colorMode() == 2) {
            Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        }
    }
}
