package de.turboman.edit.shared.project;

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

        for (var x = 0; x < unpacker.unpackArrayHeader(); x++) {
            files.add(readProjectFile(unpacker));
        }

        var timelines = new ArrayList<TimelineData>();

        for (var x = 0; x < unpacker.unpackArrayHeader(); x++) {
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

        var isVideo = unpacker.unpackBoolean();
        var audio = new ArrayList<AudioObject>();
        int videoHeight = -1;
        int videoWidth = -1;
        int videoFPS = -1;

        if (isVideo) {
            videoHeight = unpacker.unpackInt();
            videoWidth = unpacker.unpackInt();
            videoFPS = unpacker.unpackInt();

            for (var x = 0; x < unpacker.unpackArrayHeader(); x++) {
                audio.add(readAudioObject(unpacker));
            }
        }

        return new ProjectFile(name, path, type, previewImage, isVideo, videoHeight, videoWidth, videoFPS, audio);
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

        for (var x = 0; x < unpacker.unpackArrayHeader(); x++) {
            videoLayers.add(readTimelineLayer(unpacker));
        }

        var audioLayers = new ArrayList<TimelineLayer>();

        for (var x = 0; x < unpacker.unpackArrayHeader(); x++) {
            audioLayers.add(readTimelineLayer(unpacker));
        }

        var clips = new ArrayList<TimelineClip>();

        for (var x = 0; x < unpacker.unpackArrayHeader(); x++) {
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
        var position = unpacker.unpackInt();
        var startTime = unpacker.unpackLong();
        var endTime = unpacker.unpackLong();

        return new TimelineClip(file, position, startTime, endTime);
    }
}
