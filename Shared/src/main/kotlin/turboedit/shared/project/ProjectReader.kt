package turboedit.shared.project

import org.msgpack.core.MessagePack
import org.msgpack.core.MessageUnpacker
import java.io.File
import java.io.FileInputStream
import java.io.IOException

object ProjectReader {
    @Throws(IOException::class)
    fun read(path: String): Project {
        val file = File(path)
        val stream = FileInputStream(file)
        val unpacker = MessagePack.newDefaultUnpacker(stream)

        val fileVersion = unpacker.unpackInt()
        val name = unpacker.unpackString()

        val files = ArrayList<ProjectFile>()

        val fileHeader = unpacker.unpackArrayHeader()
        for (x in 0..<fileHeader) {
            files.add(readProjectFile(unpacker))
        }

        val timelines = ArrayList<TimelineData>()

        val timelineHeader = unpacker.unpackArrayHeader()
        for (x in 0..<timelineHeader) {
            timelines.add(readTimelineData(unpacker))
        }

        unpacker.close()

        return Project(path, name, fileVersion, files, timelines)
    }

    @Throws(IOException::class)
    private fun readProjectFile(unpacker: MessageUnpacker): ProjectFile {
        val name = unpacker.unpackString()
        val path = unpacker.unpackString()
        val type = unpacker.unpackString()
        val previewImage = ByteArray(unpacker.unpackArrayHeader())

        for (b in previewImage.indices) {
            previewImage[b] = unpacker.unpackByte()
        }

        val hash = unpacker.unpackString()
        val size = unpacker.unpackLong()

        val isVideo = unpacker.unpackBoolean()
        var videoWidth = -1
        var videoHeight = -1
        var videoFPS = -1
        var videoCodec: String? = ""
        val audio = ArrayList<AudioObject>()

        if (isVideo) {
            videoWidth = unpacker.unpackInt()
            videoHeight = unpacker.unpackInt()
            videoFPS = unpacker.unpackInt()
            videoCodec = unpacker.unpackString()

            val arrayHeader = unpacker.unpackArrayHeader()

            for (x in 0..<arrayHeader) {
                audio.add(readAudioObject(unpacker))
            }
        }

        return ProjectFile(
            name,
            path,
            type,
            previewImage,
            hash,
            size,
            isVideo,
            videoHeight,
            videoWidth,
            videoFPS,
            videoCodec,
            audio
        )
    }

    @Throws(IOException::class)
    private fun readAudioObject(unpacker: MessageUnpacker): AudioObject {
        val layer = unpacker.unpackInt()
        val layerName = unpacker.unpackString()
        val waveform = ByteArray(unpacker.unpackArrayHeader())

        for (b in waveform.indices) {
            waveform[b] = unpacker.unpackByte()
        }

        return AudioObject(layer, layerName, waveform)
    }

    @Throws(IOException::class)
    private fun readTimelineData(unpacker: MessageUnpacker): TimelineData {
        val name = unpacker.unpackString()
        val length = unpacker.unpackLong()

        val videoLayers = ArrayList<TimelineLayer>()
        val videoLayerHeader = unpacker.unpackArrayHeader()
        for (x in 0..<videoLayerHeader) {
            videoLayers.add(readTimelineLayer(unpacker))
        }

        val audioLayers = ArrayList<TimelineLayer>()
        val audioLayerHeader = unpacker.unpackArrayHeader()
        for (x in 0..<audioLayerHeader) {
            audioLayers.add(readTimelineLayer(unpacker))
        }

        val clips = ArrayList<TimelineClip>()
        val clipsHeader = unpacker.unpackArrayHeader()
        for (x in 0..<clipsHeader) {
            clips.add(readTimelineClip(unpacker))
        }

        return TimelineData(name, length, videoLayers, audioLayers, clips)
    }

    @Throws(IOException::class)
    private fun readTimelineLayer(unpacker: MessageUnpacker): TimelineLayer {
        val name = unpacker.unpackString()
        val enabled = unpacker.unpackBoolean()

        return TimelineLayer(name, enabled)
    }

    @Throws(IOException::class)
    private fun readTimelineClip(unpacker: MessageUnpacker): TimelineClip {
        val file = unpacker.unpackInt()
        val position = unpacker.unpackLong()
        val startTime = unpacker.unpackLong()
        val endTime = unpacker.unpackLong()

        return TimelineClip(file, position, startTime, endTime)
    }
}