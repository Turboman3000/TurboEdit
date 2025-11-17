package turboedit.editor.panes

import javafx.application.Platform
import javafx.scene.image.ImageView
import javafx.scene.layout.GridPane
import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.Frame
import org.bytedeco.javacv.JavaFXFrameConverter
import java.nio.ByteBuffer
import java.nio.ShortBuffer
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.LineUnavailableException
import javax.sound.sampled.SourceDataLine

class TestPane : GridPane() {
    init {
        val videoPath = "C:\\Users\\legon\\Nextcloud2\\Turboman\\Streams\\01_UNSORTIERT\\2025-09-15 15-14-28.mp4"
        val grabber = FFmpegFrameGrabber(videoPath)
        val converter = JavaFXFrameConverter()
        val imageView = ImageView()

        try {
            grabber.start()

            grabber.setVideoCodecName("h264_qsv")
            grabber.setAudioStream(6)

            //     grabber.setFrameRate(60.0);
            val formatContext = grabber.formatContext
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val sampleRate = grabber.getSampleRate()
        val channels = grabber.getAudioChannels()

        val audioFormat = AudioFormat(
            sampleRate.toFloat(),
            16,
            channels,
            true,
            false
        )

        val line: SourceDataLine

        try {
            line = AudioSystem.getSourceDataLine(audioFormat)

            line.open(audioFormat, line.bufferSize)
            line.start()
        } catch (e: LineUnavailableException) {
            throw RuntimeException(e)
        }

        Thread(Runnable {
            try {
                var frame: Frame
                val frameRate = grabber.getFrameRate()
                val delay = (1000 / frameRate).toLong()

                while ((grabber.grabImage().also { frame = it }) != null) {
                    val fxImage = converter.convert(frame)

                    val finalFrame = frame
                    Platform.runLater(Runnable {
                        if (fxImage != null) {
                            imageView.image = fxImage
                        } else {
                            val shortBuffer = finalFrame.samples[0] as ShortBuffer
                            val bufferSize = shortBuffer.capacity() * 2
                            val buffer = ByteArray(bufferSize)

                            val byteBuffer = ByteBuffer.wrap(buffer)
                            byteBuffer.asShortBuffer().put(shortBuffer)

                            line.write(buffer, 0, bufferSize)
                        }
                    })

                    Thread.sleep(delay)
                }
            } catch (e: Exception) {
                System.err.println("Error playing video file: " + e.message)
            } finally {
                try {
                    grabber.stop()
                    grabber.release()
                } catch (e: FFmpegFrameGrabber.Exception) {
                    e.printStackTrace()
                }
            }
        }).start()

        val autoScale = 3840.0 / grabber.getImageWidth()
        val scale = 0.3

        imageView.fitHeight = grabber.getImageHeight() * scale * autoScale
        imageView.fitWidth = grabber.getImageWidth() * scale * autoScale

        add(imageView, 0, 0, 1, 1)
    }
}
