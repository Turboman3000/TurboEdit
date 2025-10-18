package org.turbomedia.turboedit.shared.project;

import org.msgpack.core.annotations.Nullable;

import java.util.ArrayList;

public record ProjectFile(String name, String path, String type, byte[] previewImage, String hash, long size,
                          boolean isVideo, @Nullable int videoWidth, @Nullable int videoHeight, @Nullable int videoFPS,
                          String videoCodec, @Nullable ArrayList<AudioObject> audio) {
}
