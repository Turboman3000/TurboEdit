package org.turbomedia.turboedit.renderer;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.turbomedia.turboedit.renderer.websocket.RendererServer;
import org.turbomedia.turboedit.shared.project.ProjectReader;

import java.io.File;
import java.io.IOException;

public class Renderer {
    public static final int WEBSOCKET_PORT = 59992;
    public static volatile boolean IS_RUNNING = true;

    private static final Logger logger = LoggerFactory.getLogger(Renderer.class);

    public static void main(String[] args) throws ParseException, IOException {
        var options = new Options();

        // OPTIONS
        {
            options.addOption(Option.builder("?").longOpt("help").desc("Prints this Help Message").get());
            options.addOption(Option.builder("ns").longOpt("no-server").desc("Disables the WebSocket server").get());
            options.addOption(Option.builder("p").longOpt("project").desc("Defines a file to render").hasArg().get());
            options.addOption(Option.builder("vc").longOpt("video-codec").desc("Set's video Encoder").hasArg().get());
            options.addOption(Option.builder("ac").longOpt("audio-codec").desc("Set's audio Encoder").hasArg().get());
            options.addOption(Option.builder("e").longOpt("export").desc("Set's export path").hasArg().get());
        }

        var helper = HelpFormatter.builder().get();
        var cmdParser = new BasicParser();
        var cmd = cmdParser.parse(options, args);

        if (cmd.hasOption("?")) {
            helper.printHelp(" ", options);
            System.exit(0);
        }

        logger.info("Loading Rendering Server...");

        if (cmd.hasOption("p")) {
            var thread = new Thread(() -> {
                try {
                    var projectPath = cmd.getOptionValue("p");
                    var project = ProjectReader.Read(projectPath);

                    var ffmpeg = new FFmpeg("ffmpeg");
                    var ffprobe = new FFprobe("ffprobe");
                    var item = RenderBuilder.BuildFfmpeg(project, 0, ffmpeg, ffprobe, projectPath, cmd.getOptionValue("e"));
                    var executor = new FFmpegExecutor(ffmpeg, ffprobe);
                    var job = executor.createJob(item.builder(), (progress) -> {
                        if (!progress.isEnd()) return;

                        logger.info("Rendering Done!");

                        new Thread(() -> {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }

                            for (var fi : item.deletions()) {
                                var file = new File(fi);
                                file.delete();
                            }
                        }).start();
                    });

                    logger.info("Rendering Started");
                    job.run();
                } catch (Exception exp) {
                    logger.error("{} = {}", exp.getClass().getName(), exp.getMessage());
                }
            });

            thread.setName("Render-0");
            thread.setPriority(Thread.MAX_PRIORITY);
            thread.start();
        }

        if (!cmd.hasOption("ns")) {
            var thread = new Thread(() -> {
                var server = new RendererServer();
                server.start();
            });

            thread.setName("WebsocketServer-0");
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();
        } else {
            if (!cmd.hasOption("p")) {
                logger.info("WebSocket Server was disabled, and no Project is specified. Program will be closed!");
                System.exit(0);
                return;
            }

            logger.info("WebSocket Server Disabled!");
        }

        while (IS_RUNNING) {
            Thread.onSpinWait();
        }
    }
}
