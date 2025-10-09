package de.turboman.edit.editor;

import org.msgpack.core.MessagePack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

import static de.turboman.edit.editor.Editor.APPDATA;

public class PreferencesFile {
    private static final int CURRENT_FILE_VERSION = 0;
    private static final String PREFERENCES_PATH = Path.of(APPDATA, "preferences.dat").toString();
    public static Preferences CURRENT_PREFERENCES;

    public static void Read() throws IOException, InterruptedException {
        var file = new File(PREFERENCES_PATH);

        if (!file.exists()) {
            CURRENT_PREFERENCES = new Preferences(CURRENT_FILE_VERSION, "en_us", 0);
            Write();
            return;
        }

        var stream = new FileInputStream(file);
        var unpacker = MessagePack.newDefaultUnpacker(stream);

        var fileVersion = unpacker.unpackInt();
        var language = unpacker.unpackString();
        var colorMode = unpacker.unpackInt();

        CURRENT_PREFERENCES = new Preferences(fileVersion, language, colorMode);
    }

    public static void Write() throws IOException, InterruptedException {
        var file = new File(PREFERENCES_PATH);

        if (!file.exists()) {
            var parentDir = new File(Path.of(PREFERENCES_PATH).getParent().toString());

            if (!parentDir.exists()) {
                if (!parentDir.mkdirs()) {
                    throw new IOException("Could not create dirs for: " + PREFERENCES_PATH);
                }

                Thread.sleep(2000);
            }


            if (!file.createNewFile()) {
                throw new IOException("Could not create: " + PREFERENCES_PATH);
            }

            Thread.sleep(2000);
        }

        var stream = new FileOutputStream(file);
        var packer = MessagePack.newDefaultPacker(stream);

        packer.packInt(CURRENT_FILE_VERSION); // FILE VERSION
        packer.packString(CURRENT_PREFERENCES.language); // LANGUAGE
        packer.packInt(CURRENT_PREFERENCES.colorMode); // COLOR MODE - 0 = SYSTEM ; 1 = DARK ; 2 = LIGHT

        packer.close();
    }

    public static final class Preferences {
        private final int fileVersion;
        public String language;
        public int colorMode;

        public Preferences(int fileVersion, String language, int colorMode) {
            this.fileVersion = fileVersion;
            this.language = language;
            this.colorMode = colorMode;
        }

        public int fileVersion() {
            return fileVersion;
        }

        public String language() {
            return language;
        }

        public int colorMode() {
            return colorMode;
        }

        public void language(String value) throws IOException, InterruptedException {
            language = value;
            Write();
        }

        public void colorMode(int value) throws IOException, InterruptedException {
            colorMode = value;
            Editor.SetColorMode();
            Write();
        }
    }
}
