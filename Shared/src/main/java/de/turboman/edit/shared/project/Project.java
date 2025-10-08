package de.turboman.edit.shared.project;

import java.util.ArrayList;

public record Project(String path, String name, int fileVersion, ArrayList<ProjectFile> files,
                      ArrayList<TimelineData> timelines) {
}
