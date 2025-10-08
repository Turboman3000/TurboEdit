package de.turboman.edit.editor;

import de.turboman.edit.shared.project.Project;
import de.turboman.edit.shared.project.ProjectReader;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

import static de.turboman.edit.editor.Editor.TITLE;

public class ProjectManager {
    public static Project CURRENT_PROJECT;
    public final static String EXTENSION = ".tvp";
    public final static int CURRENT_VERSION = 0;

    public static void LoadProject(File path, Stage mainStage) throws IOException {
        var project = ProjectReader.Read(path.getAbsolutePath());

        System.out.println(project);
        CURRENT_PROJECT = project;

        mainStage.setTitle(TITLE + " [" + project.path() + "]");
    }
}
