package de.turboman.edit.renderer.websocket;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import de.turboman.edit.shared.protocol.ProtocolQueue;

public class WebsocketQueue {
    public static byte[] GetQueueInfoData(byte[] data) {
        var previewImage = ByteString.copyFrom(new byte[0]);
        ProtocolQueue.QueueInfoResponse.Builder builder = null;
        try {
            builder = ProtocolQueue.QueueInfoResponse
                    .newBuilder()
                    .setIndex(0)
                    .setName("Test Project")
                    .setTimeline("Test Timeline")
                    .setProjectPath("C:/PROJECT.tvp")
                    .setOutputPath("C:/OUTPUT.mp4")

                    .setVideoPreview(previewImage)
                    .setVideoWidth(1920)
                    .setVideoHeight(1080)
                    .setFrameRate(60.0f)
                    .setVideoCodec("AV1 (Copy)")

                    .setAudioStreams(1)
                    .setAudioChannels(0, 2)
                    .setAudioBitrate(0,320)
                    .setAudioSampling(0,48000)
                    .setAudioCodec(0, "AAC")

                    .setProgress(ProtocolQueue.QueueProgressResponse.parseFrom(GetQueueProgressData(data)));
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }

        return builder.build().toByteArray();
    }

    public static byte[] GetQueueProgressData(byte[] data) {
        var builder = ProtocolQueue.QueueProgressResponse.newBuilder();

        return builder.build().toByteArray();
    }
}
