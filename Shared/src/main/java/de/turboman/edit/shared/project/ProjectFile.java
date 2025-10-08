package de.turboman.edit.shared.project;

import org.msgpack.core.annotations.Nullable;

import java.util.ArrayList;

public record ProjectFile(String name, String path, String type, byte[] previewImage, boolean isVideo,
                          @Nullable int videoHeight, @Nullable int videoWidth, @Nullable int videoFPS, @Nullable
                          ArrayList<AudioObject> audio) {
}
