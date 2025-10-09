package org.turbomedia.turboedit.editor;

import javafx.stage.Stage;
import org.turbomedia.turboedit.shared.project.Project;
import org.turbomedia.turboedit.shared.project.ProjectReader;

import java.io.File;
import java.io.IOException;

import static org.turbomedia.turboedit.editor.Editor.TITLE;

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
