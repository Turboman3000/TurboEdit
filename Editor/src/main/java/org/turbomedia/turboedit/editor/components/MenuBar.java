package org.turbomedia.turboedit.editor.components;

import org.turbomedia.turboedit.editor.ProjectManager;
import org.turbomedia.turboedit.editor.windows.AboutWindow;
import org.turbomedia.turboedit.editor.windows.NewProjectWindow;
import org.turbomedia.turboedit.editor.windows.PreferencesWindow;
import org.turbomedia.turboedit.editor.windows.RenderQueueWindow;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.turbomedia.turboedit.editor.Editor.TITLE;
import static org.turbomedia.turboedit.editor.misc.Locale.GetText;

public class MenuBar extends javafx.scene.control.MenuBar {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public MenuBar(Stage stage) {
        getMenus().add(getMenuFile(stage));
        getMenus().add(getMenuEdit());

        getMenus().add(getMenuHelp());
    }

    private Menu getMenuFile(Stage stage) {
        var menu = new Menu(GetText("menubar.file"));

        var itemNewProject = new MenuItem(GetText("menubar.file.new_project"));

        itemNewProject.setOnAction(event -> new NewProjectWindow(stage));

        menu.getItems().add(itemNewProject);

        var itemLoadProject = new MenuItem(GetText("menubar.file.load_project"));
        itemLoadProject.setOnAction(event -> {
            var chooser = new FileChooser();

            chooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter(GetText("file_chooser.ext.tvp"), "*.tvp")
            );

            chooser.setTitle(TITLE + " - " + GetText("title.open_project"));
            var file = chooser.showOpenDialog(stage);

            try {
                ProjectManager.LoadProject(file, stage);
            } catch (IOException e) {
                logger.error("{} = {}", e.getClass().getName(), e.getMessage());
            }
        });

        menu.getItems().add(itemLoadProject);

        var itemPreferences = new MenuItem(GetText("menubar.file.preferences"));
        itemPreferences.setOnAction(event -> new PreferencesWindow());
        menu.getItems().add(itemPreferences);

        var renderQueueMenu = new MenuItem(GetText("menubar.file.render_queue"));
        renderQueueMenu.setOnAction(event -> new RenderQueueWindow());
        menu.getItems().add(renderQueueMenu);

        var itemQuit = new MenuItem(GetText("menubar.file.quit"));
        itemQuit.setOnAction(event -> System.exit(0));

        menu.getItems().add(itemQuit);

        return menu;
    }

    private Menu getMenuEdit() {
        var menu = new Menu(GetText("menubar.edit"));

        return menu;
    }

    private Menu getMenuHelp() {
        var menu = new Menu(GetText("menubar.help"));

        var itemAbout = new MenuItem(GetText("menubar.help.about", TITLE));
        itemAbout.setOnAction(event -> new AboutWindow());

        menu.getItems().add(itemAbout);

        var itemGithub = new MenuItem(GetText("menubar.help.repo"));
        itemGithub.setOnAction(event -> {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                try {
                    Desktop.getDesktop().browse(new URI("https://github.com/Turboman3000/TurboEdit"));
                } catch (IOException | URISyntaxException e) {
                    logger.error("{} = {}", e.getClass().getName(), e.getMessage());
                }
            }
        });
        menu.getItems().add(itemGithub);

        return menu;
    }
}
