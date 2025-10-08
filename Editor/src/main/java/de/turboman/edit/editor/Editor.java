package de.turboman.edit.editor;

import atlantafx.base.theme.PrimerDark;
import de.turboman.edit.editor.components.MenuBar;
import de.turboman.edit.editor.panels.TestPanel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class Editor extends Application {

    public final static String TITLE = "TurboEdit";
    public static Image ICON;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        ICON = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icon.png")));
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());

        var box = new VBox();

        box.setFillWidth(true);
        box.getChildren().add(new MenuBar(stage));
        box.getChildren().add(new TestPanel());

        stage.setOnCloseRequest(event -> System.exit(0));

        stage.setScene(new Scene(box));

        stage.getIcons().add(ICON);
        stage.setTitle(TITLE);
        stage.setMaximized(true);
        stage.show();
    }
}
