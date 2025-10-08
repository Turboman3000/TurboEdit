package de.turboman.edit.editor.components;

import de.turboman.edit.editor.ProjectManager;
import de.turboman.edit.editor.windows.AboutWindow;
import de.turboman.edit.editor.windows.NewProjectWindow;
import de.turboman.edit.editor.windows.PreferencesWindow;
import de.turboman.edit.editor.windows.RenderQueueWindow;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static de.turboman.edit.editor.Editor.TITLE;

public class MenuBar extends javafx.scene.control.MenuBar {
    public MenuBar(Stage stage) {
        getMenus().add(getMenuFile(stage));
        getMenus().add(getMenuEdit());

        getMenus().add(getMenuHelp());
    }

    private Menu getMenuFile(Stage stage) {
        var menu = new Menu("File");

        var itemNewProject = new MenuItem("New Project");

        itemNewProject.setOnAction(event -> new NewProjectWindow(stage));

        menu.getItems().add(itemNewProject);

        var itemLoadProject = new MenuItem("Load Project...");
        itemLoadProject.setOnAction(event -> {
            var chooser = new FileChooser();

            chooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("TurboEdit Project File", "*.tvp")
            );

            chooser.setTitle(TITLE + " - Open Project");
            var file = chooser.showOpenDialog(stage);

            try {
                ProjectManager.LoadProject(file, stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        menu.getItems().add(itemLoadProject);

        var itemPreferences = new MenuItem("Preferences");
        itemPreferences.setOnAction(event -> new PreferencesWindow());
        menu.getItems().add(itemPreferences);

        var renderQueueMenu = new MenuItem("Render Queue");
        renderQueueMenu.setOnAction(event -> new RenderQueueWindow());
        menu.getItems().add(renderQueueMenu);

        var itemQuit = new MenuItem("Quit");
        itemQuit.setOnAction(event -> System.exit(0));

        menu.getItems().add(itemQuit);

        return menu;
    }

    private Menu getMenuEdit() {
        var menu = new Menu("Edit");

        return menu;
    }

    private Menu getMenuHelp() {
        var menu = new Menu("Help");

        var itemAbout = new MenuItem("About");
        itemAbout.setOnAction(event -> new AboutWindow());

        menu.getItems().add(itemAbout);

        var itemGithub = new MenuItem("Github Repository");
        itemGithub.setOnAction(event -> {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                try {
                    Desktop.getDesktop().browse(new URI("https://github.com/Turboman3000/TurboEdit"));
                } catch (IOException | URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        menu.getItems().add(itemGithub);

        return menu;
    }
}
