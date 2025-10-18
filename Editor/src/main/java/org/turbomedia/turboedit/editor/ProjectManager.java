package org.turbomedia.turboedit.editor;

import org.turbomedia.turboedit.editor.events.EventSystem;
import org.turbomedia.turboedit.editor.events.EventType;
import org.turbomedia.turboedit.shared.project.Project;
import org.turbomedia.turboedit.shared.project.ProjectReader;

import java.io.File;
import java.io.IOException;

public class ProjectManager {
    public static Project CURRENT_PROJECT;
    public final static String EXTENSION = ".tvp";
    public final static int CURRENT_VERSION = 0;

    public static void LoadProject(File path) throws IOException {
        CURRENT_PROJECT = ProjectReader.Read(path.getAbsolutePath());

        EventSystem.CallEvent(EventType.LOADED_PROJECT, CURRENT_PROJECT);
        EventSystem.CallEvent(EventType.DISCORD_UPDATE_ACTIVITY);
    }
}
