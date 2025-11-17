package turboedit.renderer

import net.bramp.ffmpeg.FFmpeg
import net.bramp.ffmpeg.FFmpegExecutor
import net.bramp.ffmpeg.FFprobe
import net.bramp.ffmpeg.progress.Progress
import net.bramp.ffmpeg.progress.ProgressListener
import org.apache.commons.cli.BasicParser
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Option
import org.apache.commons.cli.Options
import org.apache.commons.cli.ParseException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import turboedit.renderer.websocket.RendererServer
import turboedit.shared.project.ProjectReader
import java.io.File
import java.io.IOException
import kotlin.concurrent.Volatile
import kotlin.system.exitProcess

object Renderer {
    const val WEBSOCKET_PORT: Int = 59992

    @Volatile
    var IS_RUNNING: Boolean = true

    private val logger: Logger = LoggerFactory.getLogger(Renderer::class.java)

    @Throws(ParseException::class, IOException::class)
    fun main(args: Array<String>) {
        val options = Options()

        // OPTIONS
        run {
            options.addOption(Option.builder("?").longOpt("help").desc("Prints this Help Message").get())
            options.addOption(Option.builder("ns").longOpt("no-server").desc("Disables the WebSocket server").get())
            options.addOption(Option.builder("p").longOpt("project").desc("Defines a file to render").hasArg().get())
            options.addOption(Option.builder("vc").longOpt("video-codec").desc("Set's video Encoder").hasArg().get())
            options.addOption(Option.builder("ac").longOpt("audio-codec").desc("Set's audio Encoder").hasArg().get())
            options.addOption(Option.builder("e").longOpt("export").desc("Set's export path").hasArg().get())
        }

        val helper = HelpFormatter.builder().get()
        val cmdParser = BasicParser()
        val cmd = cmdParser.parse(options, args)

        if (cmd.hasOption("?")) {
            helper.printHelp(" ", options)
            exitProcess(0)
        }

        logger.info("Loading Rendering Server...")

        if (cmd.hasOption("p")) {
            val thread = Thread {
                try {
                    val projectPath = cmd.getOptionValue("p")
                    val project = ProjectReader.read(projectPath)

                    val ffmpeg = FFmpeg("ffmpeg")
                    val ffprobe = FFprobe("ffprobe")
                    val item =
                        RenderBuilder.buildFFMPEG(project, 0, ffmpeg, ffprobe, projectPath, cmd.getOptionValue("e"))
                    val executor = FFmpegExecutor(ffmpeg, ffprobe)

                    val job = executor.createJob(item.builder, ProgressListener { progress: Progress? ->
                        if (!progress!!.isEnd) return@ProgressListener;

                        logger.info("Rendering Done!")

                        Thread(Runnable {
                            try {
                                Thread.sleep(1000)
                            } catch (e: InterruptedException) {
                                throw RuntimeException(e)
                            }
                            for (fi in item.deletions!!) {
                                val file = File(fi)
                                file.delete()
                            }
                        }).start()
                    })

                    logger.info("Rendering Started")
                    job.run()
                } catch (exp: Exception) {
                    logger.error("{} = {}", exp.javaClass.getName(), exp.message)
                }
            }

            thread.setName("Render-0")
            thread.setPriority(Thread.MAX_PRIORITY)
            thread.start()
        }

        if (!cmd.hasOption("ns")) {
            val thread = Thread(Runnable {
                val server = RendererServer()
                server.start()
            }, "WebsocketServer-0")

            thread.setPriority(Thread.MIN_PRIORITY)
            thread.start()
        } else {
            if (!cmd.hasOption("p")) {
                logger.info("WebSocket Server was disabled, and no Project is specified. Program will be closed!")
                exitProcess(0)
            }

            logger.info("WebSocket Server Disabled!")
        }

        while (IS_RUNNING) {
            Thread.onSpinWait()
        }
    }
}