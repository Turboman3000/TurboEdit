package org.turbomedia.turboedit.editor.misc;

import org.msgpack.core.MessagePack;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;
import org.turbomedia.turboedit.editor.renderer.FileResolveMethod;
import org.turbomedia.turboedit.editor.renderer.RenderServerEntry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.turbomedia.turboedit.editor.Editor.APPDATA;

public class PreferencesFile {
    private static final int CURRENT_FILE_VERSION = 0;
    private static final String PREFERENCES_PATH = Path.of(APPDATA, "preferences").toString();
    public static Preferences CURRENT_PREFERENCES;

    public static void Read() throws IOException, InterruptedException {
        var file = new File(PREFERENCES_PATH);

        if (!file.exists()) {
            var buildInServer = new RenderServerEntry("Build-In", "127.0.0.1", FileResolveMethod.MAPPING, true, true);

            CURRENT_PREFERENCES = new Preferences(CURRENT_FILE_VERSION, "en_us", 0, false, List.of(buildInServer));
            Write();

            return;
        }

        var stream = new FileInputStream(file);
        var unpacker = MessagePack.newDefaultUnpacker(stream);

        var fileVersion = unpacker.unpackInt();
        var language = unpacker.unpackString();
        var colorMode = unpacker.unpackInt();
        var showIPsForServers = unpacker.unpackBoolean();

        var renderServers = unpackRenderServers(unpacker);

        CURRENT_PREFERENCES = new Preferences(fileVersion, language, colorMode, showIPsForServers, renderServers);
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
        packer.packBoolean(CURRENT_PREFERENCES.showIPsForServers); // SHOW IPS FOR SERVER
        packRenderServers(packer, CURRENT_PREFERENCES.renderServers); // RENDER SERVERS

        packer.close();
    }

    private static List<RenderServerEntry> unpackRenderServers(MessageUnpacker unpacker) throws IOException {
        var list = new ArrayList<RenderServerEntry>();
        var header = unpacker.unpackArrayHeader();

        for (var x = 0; x < header; x++) {
            var displayName = unpacker.unpackString();
            var ip = unpacker.unpackString();
            var fileResolverMethod = unpacker.unpackInt();
            var defaultServer = unpacker.unpackBoolean();
            var buildIn = unpacker.unpackBoolean();

            list.add(new RenderServerEntry(displayName, ip, FileResolveMethod.values()[fileResolverMethod], defaultServer, buildIn));
        }

        return list;
    }

    private static void packRenderServers(MessagePacker packer, List<RenderServerEntry> entries) throws IOException {
        packer.packArrayHeader(entries.size());

        for (var entry : entries) {
            packer.packString(entry.displayName());
            packer.packString(entry.ip());
            packer.packInt(entry.fileResolveMethod().ordinal());
            packer.packBoolean(entry.defaultServer());
            packer.packBoolean(entry.buildIn());
        }
    }

    public static final class Preferences {
        private final int fileVersion;
        public String language;
        public int colorMode;
        public boolean showIPsForServers;
        public List<RenderServerEntry> renderServers;

        public Preferences(
                int fileVersion,
                String language,
                int colorMode,
                boolean showIPsForServers,
                List<RenderServerEntry> renderServers
        ) {
            this.fileVersion = fileVersion;
            this.language = language;
            this.colorMode = colorMode;
            this.showIPsForServers = showIPsForServers;
            this.renderServers = renderServers;
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
            var old = language;
            language = value;
            Write();
            language = old;
        }

        public void colorMode(int value) throws IOException, InterruptedException {
            colorMode = value;
            StyleManager.UpdateStyle();
            Write();
        }

        public void showIPsForServers(boolean value) throws IOException, InterruptedException {
            showIPsForServers = value;
            Write();
        }

        public void addRenderServers(RenderServerEntry entry) throws IOException, InterruptedException {
            if (entry.defaultServer()) {
                for (var serverEntry : renderServers) {
                    serverEntry.defaultServer(false);
                }
            }

            renderServers.add(entry);
            Write();
        }

        public void removeRenderServers(RenderServerEntry entry) throws IOException, InterruptedException {
            renderServers.remove(entry);
            Write();
        }
    }
}
