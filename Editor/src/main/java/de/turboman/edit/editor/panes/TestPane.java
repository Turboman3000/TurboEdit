package de.turboman.edit.editor.panes;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.bytedeco.ffmpeg.avformat.AVFormatContext;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.JavaFXFrameConverter;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

public class TestPane extends GridPane {
    public TestPane() {
        var videoPath = "C:\\Users\\legon\\Nextcloud2\\Turboman\\Streams\\01_UNSORTIERT\\2025-09-15 15-14-28.mp4";
        var grabber = new FFmpegFrameGrabber(videoPath);
        var converter = new JavaFXFrameConverter();
        var imageView = new ImageView();

        try {
            grabber.start();

            grabber.setVideoCodecName("h264_qsv");
            grabber.setAudioStream(6);
            //     grabber.setFrameRate(60.0);

            AVFormatContext formatContext = grabber.getFormatContext();

            if (formatContext == null) {
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        int sampleRate = grabber.getSampleRate();
        int channels = grabber.getAudioChannels();

        AudioFormat audioFormat = new AudioFormat(
                (float) sampleRate,
                16,
                channels,
                true,
                false
        );

        SourceDataLine line;

        try {
            line = AudioSystem.getSourceDataLine(audioFormat);

            line.open(audioFormat, line.getBufferSize());
            line.start();
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            try {
                Frame frame;
                double frameRate = grabber.getFrameRate();
                long delay = (long) (1000 / frameRate);

                while ((frame = grabber.grabImage()) != null) {
                    final Image fxImage = converter.convert(frame);

                    var finalFrame = frame;
                    Platform.runLater(() -> {
                        if (fxImage != null) {
                            imageView.setImage(fxImage);
                        } else  {
                            var shortBuffer = (ShortBuffer) finalFrame.samples[0];
                            var bufferSize = shortBuffer.capacity() * 2;
                            var buffer = new byte[bufferSize];

                            var byteBuffer = ByteBuffer.wrap(buffer);
                            byteBuffer.asShortBuffer().put(shortBuffer);

                            line.write(buffer, 0, bufferSize);
                        }
                    });

                    Thread.sleep(delay);
                }
            } catch (Exception e) {
                System.err.println("Error playing video file: " + e.getMessage());
            } finally {
                try {
                    grabber.stop();
                    grabber.release();
                } catch (FFmpegFrameGrabber.Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        double autoScale = (double) 3840 / grabber.getImageWidth();
        double scale = 0.3;

        imageView.setFitHeight(grabber.getImageHeight() * scale * autoScale);
        imageView.setFitWidth(grabber.getImageWidth() * scale * autoScale);

        add(imageView, 0, 0, 1, 1);
    }
}
