package org.turbomedia.turboedit.shared.project;

import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ProjectReader {
    public static Project Read(String path) throws IOException {
        var file = new File(path);
        var stream = new FileInputStream(file);
        var unpacker = MessagePack.newDefaultUnpacker(stream);

        var fileVersion = unpacker.unpackInt();
        var name = unpacker.unpackString();

        var files = new ArrayList<ProjectFile>();

        var fileHeader = unpacker.unpackArrayHeader();
        for (var x = 0; x < fileHeader; x++) {
            files.add(readProjectFile(unpacker));
        }

        var timelines = new ArrayList<TimelineData>();

        var timelineHeader = unpacker.unpackArrayHeader();
        for (var x = 0; x < timelineHeader; x++) {
            timelines.add(readTimelinedata(unpacker));
        }

        unpacker.close();

        return new Project(path, name, fileVersion, files, timelines);
    }

    private static ProjectFile readProjectFile(MessageUnpacker unpacker) throws IOException {
        var name = unpacker.unpackString();
        var path = unpacker.unpackString();
        var type = unpacker.unpackString();
        var previewImage = new byte[unpacker.unpackArrayHeader()];

        for (var b = 0; b < previewImage.length; b++) {
            previewImage[b] = unpacker.unpackByte();
        }

        var hash = unpacker.unpackString();
        var size = unpacker.unpackLong();

        var isVideo = unpacker.unpackBoolean();
        int videoWidth = -1;
        int videoHeight = -1;
        int videoFPS = -1;
        String videoCodec = "";
        var audio = new ArrayList<AudioObject>();

        if (isVideo) {
            videoWidth = unpacker.unpackInt();
            videoHeight = unpacker.unpackInt();
            videoFPS = unpacker.unpackInt();
            videoCodec = unpacker.unpackString();

            var arrayHeader = unpacker.unpackArrayHeader();

            for (var x = 0; x < arrayHeader; x++) {
                audio.add(readAudioObject(unpacker));
            }
        }

        return new ProjectFile(name, path, type, previewImage, hash, size, isVideo, videoHeight, videoWidth, videoFPS, videoCodec, audio);
    }

    private static AudioObject readAudioObject(MessageUnpacker unpacker) throws IOException {
        var layer = unpacker.unpackInt();
        var layerName = unpacker.unpackString();
        var waveform = new byte[unpacker.unpackArrayHeader()];

        for (var b = 0; b < waveform.length; b++) {
            waveform[b] = unpacker.unpackByte();
        }

        return new AudioObject(layer, layerName, waveform);
    }

    private static TimelineData readTimelinedata(MessageUnpacker unpacker) throws IOException {
        var name = unpacker.unpackString();
        var length = unpacker.unpackLong();

        var videoLayers = new ArrayList<TimelineLayer>();
        var videoLayerHeader = unpacker.unpackArrayHeader();
        for (var x = 0; x < videoLayerHeader; x++) {
            videoLayers.add(readTimelineLayer(unpacker));
        }

        var audioLayers = new ArrayList<TimelineLayer>();
        var audioLayerHeader = unpacker.unpackArrayHeader();
        for (var x = 0; x < audioLayerHeader; x++) {
            audioLayers.add(readTimelineLayer(unpacker));
        }

        var clips = new ArrayList<TimelineClip>();
        var clipsHeader = unpacker.unpackArrayHeader();
        for (var x = 0; x < clipsHeader; x++) {
            clips.add(readTimelineClip(unpacker));
        }

        return new TimelineData(name, length, videoLayers, audioLayers, clips);
    }

    private static TimelineLayer readTimelineLayer(MessageUnpacker unpacker) throws IOException {
        var name = unpacker.unpackString();
        var enabled = unpacker.unpackBoolean();

        return new TimelineLayer(name, enabled);
    }

    private static TimelineClip readTimelineClip(MessageUnpacker unpacker) throws IOException {
        var file = unpacker.unpackInt();
        var position = unpacker.unpackLong();
        var startTime = unpacker.unpackLong();
        var endTime = unpacker.unpackLong();

        return new TimelineClip(file, position, startTime, endTime);
    }
}
