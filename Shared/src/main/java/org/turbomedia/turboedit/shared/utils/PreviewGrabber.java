package org.turbomedia.turboedit.shared.utils;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegStream;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class PreviewGrabber {

    public static byte[] GetVideoPreview(FFmpeg ffmpeg, FFprobe ffprobe, String videoPath, int width, int height) throws IOException {
        var tempFile = File.createTempFile("tm_te_vprev_", ".jpg");
        var executor = new FFmpegExecutor(ffmpeg, ffprobe);
        var builder = new FFmpegBuilder()
                .setInput(videoPath)
                .addOutput(tempFile.getAbsolutePath())
                .setFrames(1)
                .setVideoFilter("select='gte(n\\,10)',scale=" + height + ":" + width)
                .done();

        var job = executor.createJob(builder);
        job.run();

        var fileData = FileUtils.readFileToByteArray(tempFile);

        if (!tempFile.delete()) {
            throw new IOException("Couldn't delete temp preview file: " + tempFile.getAbsolutePath());
        }

        return fileData;
    }

    public static byte[] GetAudioPreview(FFmpeg ffmpeg, FFprobe ffprobe, String path, int audioStream) throws IOException {
        var mimeType = Files.probeContentType(Path.of(path));
        var executor = new FFmpegExecutor(ffmpeg, ffprobe);
        ArrayList<File> tempAudioFiles = new ArrayList<>();

        if (mimeType.startsWith("video/")) {
            var probeBuilder = ffprobe.probe(path);

            for (var stream : probeBuilder.getStreams()) {
                if (stream.codec_type != FFmpegStream.CodecType.AUDIO) continue;

                var index = stream.index - 1;

                tempAudioFiles.add(File.createTempFile("tm_te_adprev_" + index + "_", ".aac"));

                if (index != audioStream) continue;

                var builder = new FFmpegBuilder()
                        .setInput(path)
                        .addOutput(tempAudioFiles.get(index).getAbsolutePath())
                        .setAudioCodec("copy")
                        .addExtraArgs("-map", "0:a:" + index)
                        .disableVideo()
                        .done();

                var job = executor.createJob(builder);
                job.run();
            }
        }

        var tempFile = File.createTempFile("tm_te_aprev_", ".png");
        var builder = new FFmpegBuilder()
                .setInput(tempAudioFiles.isEmpty() ? path : tempAudioFiles.get(audioStream).getAbsolutePath())
                .overrideOutputFiles(true)
                .setComplexFilter("[0:a]compand=points=-80/-900|-40/-15|-10/-5|0/0:gain=10,showwavespic=s=1920x480:colors=white:split_channels=0,format=rgba[v]")
                .addOutput(tempFile.getAbsolutePath())
                .addExtraArgs("-map","[v]")
                .setFrames(1)
                .done();

        var job = executor.createJob(builder);
        job.run();

        var fileData = FileUtils.readFileToByteArray(tempFile);

        if (!tempFile.delete()) {
            throw new IOException("Couldn't delete temp preview file: " + tempFile.getAbsolutePath());
        }

        if (!tempAudioFiles.isEmpty()) {
            for (var fi : tempAudioFiles) {
                if (!fi.delete()) {
                    throw new IOException("Couldn't delete temp audio file: " + fi.getAbsolutePath());
                }
            }
        }

        return fileData;
    }
}
