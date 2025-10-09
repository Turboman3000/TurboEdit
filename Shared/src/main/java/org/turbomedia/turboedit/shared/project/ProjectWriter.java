package org.turbomedia.turboedit.shared.project;

import org.msgpack.core.MessagePack;
import org.msgpack.core.MessagePacker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProjectWriter {
    public static void Write(Project project) throws IOException {
        var file = new File(project.path());

        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new RuntimeException("Could not create Project file");
            }
        }

        var stream = new FileOutputStream(file);
        var packer = MessagePack.newDefaultPacker(stream);

        packer.packInt(project.fileVersion());
        packer.packString(project.name());

        // Files
        packer.packArrayHeader(project.files().size());
        for (var x = 0; x < project.files().size(); x++) {
            writeProjectFile(packer, project.files().get(x));
        }

        // Timelines
        packer.packArrayHeader(project.timelines().size());
        for (var x = 0; x < project.timelines().size(); x++) {
            writeTimelineData(packer, project.timelines().get(x));
        }

        packer.close();
    }

    private static void writeProjectFile(MessagePacker packer, ProjectFile data) throws IOException {
        packer.packString(data.name());
        packer.packString(data.path());
        packer.packString(data.type());

        // Preview Image
        packer.packArrayHeader(data.previewImage().length);
        for (var b = 0; b < data.previewImage().length; b++) {
            packer.packByte(data.previewImage()[b]);
        }

        packer.packBoolean(data.isVideo());

        if (data.isVideo()) {
            packer.packInt(data.videoHeight());
            packer.packInt(data.videoWidth());
            packer.packInt(data.videoFPS());

            // Audio Object
            packer.packArrayHeader(data.audio().size());
            for (var x = 0; x < data.audio().size(); x++) {
                writeAudioObject(packer, data.audio().get(x));
            }
        }
    }

    private static void writeAudioObject(MessagePacker packer, AudioObject data) throws IOException {
        packer.packInt(data.layer());
        packer.packString(data.layerName());

        // Waveform
        packer.packArrayHeader(data.waveform().length);
        for (var b = 0; b < data.waveform().length; b++) {
            packer.packByte(data.waveform()[b]);
        }
    }

    private static void writeTimelineData(MessagePacker packer, TimelineData data) throws IOException {
        packer.packString(data.name());
        packer.packLong(data.length());

        // Video Layers
        packer.packArrayHeader(data.videoLayers().size());
        for (var x = 0; x < data.videoLayers().size(); x++) {
            writeTimelineLayer(packer, data.videoLayers().get(x));
        }

        // Audio Layers
        packer.packArrayHeader(data.audioLayers().size());
        for (var x = 0; x < data.audioLayers().size(); x++) {
            writeTimelineLayer(packer, data.audioLayers().get(x));
        }

        // Clips
        packer.packArrayHeader(data.clips().size());
        for (var x = 0; x < data.clips().size(); x++) {
            writeTimelineClip(packer, data.clips().get(x));
        }
    }

    private static void writeTimelineLayer(MessagePacker packer, TimelineLayer data) throws IOException {
        packer.packString(data.name());
        packer.packBoolean(data.enabled());
    }

    private static void writeTimelineClip(MessagePacker packer, TimelineClip data) throws IOException {
        packer.packInt(data.file());
        packer.packInt(data.position());
        packer.packLong(data.startTime());
        packer.packLong(data.endTime());
    }
}
