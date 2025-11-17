package turboedit.editor.misc

import org.msgpack.core.MessagePack
import org.msgpack.core.MessagePacker
import org.msgpack.core.MessageUnpacker
import turboedit.editor.renderer.FileResolveMethod
import turboedit.editor.renderer.RenderServerEntry
import turboedit.editor.renderer.RendererClient
import turboedit.editor.Editor
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Path
import java.util.ArrayList
import java.util.List
import java.util.UUID

object PreferencesFile {
    private const val CURRENT_FILE_VERSION = 0
    private val PREFERENCES_PATH = Path.of(Editor.Companion.APPDATA!!, "preferences").toString()
    @JvmField
    var CURRENT_PREFERENCES: Preferences? = null

    @Throws(IOException::class, InterruptedException::class)
    fun read() {
        val file = File(PREFERENCES_PATH)

        if (!file.exists()) {
            val buildInServer = RenderServerEntry(
                UUID.randomUUID().toString(),
                "Build-In",
                "127.0.0.1",
                FileResolveMethod.MAPPING,
                true,
                true
            )

            CURRENT_PREFERENCES =
                Preferences(CURRENT_FILE_VERSION, "en_us", 0, false, List.of<RenderServerEntry?>(buildInServer))
            write()

            return
        }

        val stream = FileInputStream(file)
        val unpacker = MessagePack.newDefaultUnpacker(stream)

        val fileVersion = unpacker.unpackInt()
        val language = unpacker.unpackString()
        val colorMode = unpacker.unpackInt()
        val showIPsForServers = unpacker.unpackBoolean()

        val renderServers = unpackRenderServers(unpacker)

        CURRENT_PREFERENCES = Preferences(fileVersion, language, colorMode, showIPsForServers, renderServers)
    }

    @Throws(IOException::class, InterruptedException::class)
    fun write() {
        val file = File(PREFERENCES_PATH)

        if (!file.exists()) {
            val parentDir = File(Path.of(PREFERENCES_PATH).parent.toString())

            if (!parentDir.exists()) {
                if (!parentDir.mkdirs()) {
                    throw IOException("Could not create dirs for: $PREFERENCES_PATH")
                }

                Thread.sleep(2000)
            }


            if (!file.createNewFile()) {
                throw IOException("Could not create: $PREFERENCES_PATH")
            }

            Thread.sleep(2000)
        }

        val stream = FileOutputStream(file)
        val packer = MessagePack.newDefaultPacker(stream)

        packer.packInt(CURRENT_FILE_VERSION) // FILE VERSION
        packer.packString(CURRENT_PREFERENCES!!.language) // LANGUAGE
        packer.packInt(CURRENT_PREFERENCES!!.colorMode) // COLOR MODE - 0 = SYSTEM ; 1 = DARK ; 2 = LIGHT
        packer.packBoolean(CURRENT_PREFERENCES!!.showIPsForServers) // SHOW IPS FOR SERVER
        packRenderServers(packer, CURRENT_PREFERENCES!!.renderServers) // RENDER SERVERS

        packer.close()
    }

    @Throws(IOException::class)
    private fun unpackRenderServers(unpacker: MessageUnpacker): MutableList<RenderServerEntry> {
        val list = ArrayList<RenderServerEntry>()
        val header = unpacker.unpackArrayHeader()

        for (x in 0..<header) {
            val id = unpacker.unpackString()
            val displayName = unpacker.unpackString()
            val ip = unpacker.unpackString()
            val fileResolverMethod = unpacker.unpackInt()
            val defaultServer = unpacker.unpackBoolean()
            val buildIn = unpacker.unpackBoolean()

            list.add(
                RenderServerEntry(
                    id,
                    displayName,
                    ip,
                    FileResolveMethod.entries[fileResolverMethod],
                    defaultServer,
                    buildIn
                )
            )
        }

        return list
    }

    @Throws(IOException::class)
    private fun packRenderServers(packer: MessagePacker, entries: MutableList<RenderServerEntry>) {
        packer.packArrayHeader(entries.size)

        for (entry in entries) {
            packer.packString(entry.id())
            packer.packString(entry.displayName())

            if (entry.ip().split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size != 2) {
                packer.packString(entry.ip() + ":" + RendererClient.DEFAULT_PORT)
            } else {
                packer.packString(entry.ip())
            }

            packer.packInt(entry.fileResolveMethod()!!.ordinal)
            packer.packBoolean(entry.defaultServer())
            packer.packBoolean(entry.buildIn())
        }
    }

    class Preferences(
        private val fileVersion: Int,
        @JvmField var language: String?,
        var colorMode: Int,
        @JvmField var showIPsForServers: Boolean,
        @JvmField var renderServers: MutableList<RenderServerEntry>
    ) {
        fun fileVersion(): Int {
            return fileVersion
        }

        fun language(): String? {
            return language
        }

        fun colorMode(): Int {
            return colorMode
        }

        @Throws(IOException::class, InterruptedException::class)
        fun language(value: String?) {
            val old = language
            language = value
            write()
            language = old
        }

        @Throws(IOException::class, InterruptedException::class)
        fun colorMode(value: Int) {
            colorMode = value
            StyleManager.updateStyle()
            write()
        }

        @Throws(IOException::class, InterruptedException::class)
        fun showIPsForServers(value: Boolean) {
            showIPsForServers = value
            write()
        }

        @Throws(IOException::class, InterruptedException::class)
        fun addRenderServers(entry: RenderServerEntry) {
            if (entry.defaultServer()) {
                for (serverEntry in renderServers) {
                    serverEntry.defaultServer(false)
                }
            } else {
                var isDefault = true

                for (serverEntry in renderServers) {
                    if (!serverEntry.defaultServer()) continue

                    isDefault = false
                }

                if (isDefault) {
                    entry.defaultServer(true)
                }
            }

            val oldServers = renderServers
            renderServers = ArrayList<RenderServerEntry>()

            renderServers.addAll(oldServers)
            renderServers.add(entry)

            write()
        }

        @Throws(IOException::class, InterruptedException::class)
        fun removeRenderServers(entry: RenderServerEntry) {
            if (entry.defaultServer() && !renderServers.isEmpty()) {
            //    renderServers.getFirst().defaultServer(true)
            }

            renderServers.remove(entry)
            write()
        }
    }
}