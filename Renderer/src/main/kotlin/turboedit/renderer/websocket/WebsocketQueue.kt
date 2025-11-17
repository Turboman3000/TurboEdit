package turboedit.renderer.websocket

import com.google.protobuf.ByteString
import com.google.protobuf.InvalidProtocolBufferException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.turbomedia.turboedit.shared.protocol.ProtocolQueue.QueueInfoResponse
import org.turbomedia.turboedit.shared.protocol.ProtocolQueue.QueueProgressResponse

object WebsocketQueue {
    private val logger: Logger = LoggerFactory.getLogger(WebsocketQueue::class.java)

    fun getQueueInfoData(data: ByteArray?): ByteArray? {
        val previewImage = ByteString.copyFrom(ByteArray(0))
        var builder: QueueInfoResponse.Builder? = null
        try {
            builder = QueueInfoResponse
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
                .setAudioBitrate(0, 320)
                .setAudioSampling(0, 48000)
                .setAudioCodec(0, "AAC")

                .setProgress(QueueProgressResponse.parseFrom(getQueueProgressData(data)))
        } catch (e: InvalidProtocolBufferException) {
            logger.error("{} = {}", e.javaClass.getName(), e.message)
        }

        checkNotNull(builder)
        return builder.build().toByteArray()
    }

    fun getQueueProgressData(data: ByteArray?): ByteArray? {
        val builder = QueueProgressResponse.newBuilder()

        return builder.build().toByteArray()
    }
}
