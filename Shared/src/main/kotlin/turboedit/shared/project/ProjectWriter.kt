package turboedit.shared.project

import org.msgpack.core.MessagePack
import org.msgpack.core.MessagePacker
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object ProjectWriter {
    @Throws(IOException::class)
    fun write(project: Project) {
        val file = File(project.path)

        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw RuntimeException("Could not create Project file")
            }
        }

        val stream = FileOutputStream(file)
        val packer = MessagePack.newDefaultPacker(stream)

        packer.packInt(project.fileVersion)
        packer.packString(project.name)

        // Files
        packer.packArrayHeader(project.files.size)
        for (x in project.files.indices) {
            writeProjectFile(packer, project.files[x])
        }

        // Timelines
        packer.packArrayHeader(project.timelines.size)
        for (x in project.timelines.indices) {
            writeTimelineData(packer, project.timelines[x])
        }

        packer.close()
    }

    @Throws(IOException::class)
    private fun writeProjectFile(packer: MessagePacker, data: ProjectFile) {
        packer.packString(data.name)
        packer.packString(data.path)
        packer.packString(data.type)

        // Preview Image
        packer.packArrayHeader(data.previewImage?.size ?: 0)
        for (b in data.previewImage!!.indices) {
            packer.packByte(data.previewImage[b])
        }

        packer.packString(data.hash)
        packer.packLong(data.size)
        packer.packBoolean(data.isVideo)

        if (data.isVideo) {
            packer.packInt(data.videoWidth!!)
            packer.packInt(data.videoHeight!!)
            packer.packInt(data.videoFPS!!)
            packer.packString(data.videoCodec)

            // Audio Object
            packer.packArrayHeader(data.audio!!.size)
            for (x in data.audio.indices) {
                writeAudioObject(packer, data.audio[x])
            }
        }
    }

    @Throws(IOException::class)
    private fun writeAudioObject(packer: MessagePacker, data: AudioObject) {
        packer.packInt(data.layer)
        packer.packString(data.layerName)

        // Waveform
        packer.packArrayHeader(data.waveform.size)
        for (b in data.waveform.indices) {
            packer.packByte(data.waveform[b])
        }
    }

    @Throws(IOException::class)
    private fun writeTimelineData(packer: MessagePacker, data: TimelineData) {
        packer.packString(data.name)
        packer.packLong(data.length)

        // Video Layers
        packer.packArrayHeader(data.videoLayers.size)
        for (x in data.videoLayers.indices) {
            writeTimelineLayer(packer, data.videoLayers[x])
        }

        // Audio Layers
        packer.packArrayHeader(data.audioLayers.size)
        for (x in data.audioLayers.indices) {
            writeTimelineLayer(packer, data.audioLayers[x])
        }

        // Clips
        packer.packArrayHeader(data.clips.size)
        for (x in data.clips.indices) {
            writeTimelineClip(packer, data.clips[x])
        }
    }

    @Throws(IOException::class)
    private fun writeTimelineLayer(packer: MessagePacker, data: TimelineLayer) {
        packer.packString(data.name)
        packer.packBoolean(data.enabled)
    }

    @Throws(IOException::class)
    private fun writeTimelineClip(packer: MessagePacker, data: TimelineClip) {
        packer.packInt(data.file)
        packer.packLong(data.position)
        packer.packLong(data.startTime)
        packer.packLong(data.endTime)
    }
}