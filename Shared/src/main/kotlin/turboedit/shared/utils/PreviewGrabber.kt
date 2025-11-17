package turboedit.shared.utils

import net.bramp.ffmpeg.FFmpeg
import net.bramp.ffmpeg.FFmpegExecutor
import net.bramp.ffmpeg.FFprobe
import net.bramp.ffmpeg.builder.FFmpegBuilder
import net.bramp.ffmpeg.probe.FFmpegStream
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

object PreviewGrabber {
    @Throws(IOException::class)
    fun getVideoPreview(ffmpeg: FFmpeg, ffprobe: FFprobe, videoPath: String?, width: Int, height: Int): ByteArray? {
        val tempFile = File.createTempFile("tm_te_vprev_", ".jpg")
        val executor = FFmpegExecutor(ffmpeg, ffprobe)
        val builder = FFmpegBuilder()
            .setInput(videoPath)
            .addOutput(tempFile.absolutePath)
            .setFrames(1)
            .setVideoFilter("select='gte(n\\,10)',scale=$height:$width")
            .done()

        val job = executor.createJob(builder)
        job.run()

        val fileData = FileUtils.readFileToByteArray(tempFile)

        if (!tempFile.delete()) {
            throw IOException("Couldn't delete temp preview file: " + tempFile.absolutePath)
        }

        return fileData
    }

    @Throws(IOException::class)
    fun getAudioPreview(ffmpeg: FFmpeg, ffprobe: FFprobe, path: String, audioStream: Int): ByteArray? {
        val mimeType = Files.probeContentType(Path.of(path))
        val executor = FFmpegExecutor(ffmpeg, ffprobe)
        val tempAudioFiles = ArrayList<File>()

        if (mimeType.startsWith("video/")) {
            val probeBuilder = ffprobe.probe(path)

            for (stream in probeBuilder.getStreams()) {
                if (stream.codec_type != FFmpegStream.CodecType.AUDIO) continue

                val index = stream.index - 1

                tempAudioFiles.add(File.createTempFile("tm_te_adprev_" + index + "_", ".aac"))

                if (index != audioStream) continue

                val builder = FFmpegBuilder()
                    .setInput(path)
                    .addOutput(tempAudioFiles[index].absolutePath)
                    .setAudioCodec("copy")
                    .addExtraArgs("-map", "0:a:$index")
                    .disableVideo()
                    .done()

                val job = executor.createJob(builder)
                job.run()
            }
        }

        val tempFile = File.createTempFile("tm_te_aprev_", ".png")
        val builder = FFmpegBuilder()
            .setInput(if (tempAudioFiles.isEmpty()) path else tempAudioFiles[audioStream].absolutePath)
            .overrideOutputFiles(true)
            .setComplexFilter("[0:a]compand=points=-80/-900|-40/-15|-10/-5|0/0:gain=10,showwavespic=s=1920x480:colors=white:split_channels=0,format=rgba[v]")
            .addOutput(tempFile.absolutePath)
            .addExtraArgs("-map", "[v]")
            .setFrames(1)
            .done()

        val job = executor.createJob(builder)
        job.run()

        val fileData = FileUtils.readFileToByteArray(tempFile)

        if (!tempFile.delete()) {
            throw IOException("Couldn't delete temp preview file: " + tempFile.absolutePath)
        }

        if (!tempAudioFiles.isEmpty()) {
            for (fi in tempAudioFiles) {
                if (!fi.delete()) {
                    throw IOException("Couldn't delete temp audio file: " + fi.absolutePath)
                }
            }
        }

        return fileData
    }
}
