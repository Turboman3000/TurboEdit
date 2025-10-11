package org.turbomedia.turboedit.renderer;

import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.turbomedia.turboedit.shared.project.Project;

import java.nio.file.Path;
import java.util.ArrayList;

public class RenderBuilder {
    public static FFmpegBuilder BuildFfmpeg(Project project, String projectPath, String output) {
        var paths = new ArrayList<String>();

        for (var file : project.files()) {
            paths.add(Path.of(projectPath.split("\\.")[0], file.path()).normalize().toAbsolutePath().toString());
        }

        var builder = new FFmpegBuilder();

        for (var path : paths) {
            builder = builder.addInput(path);
        }

        builder = builder.addOutput(output).done();

        System.out.println(builder.build());

        return builder;
    }
}
