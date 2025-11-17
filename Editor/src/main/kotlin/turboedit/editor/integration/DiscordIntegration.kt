package turboedit.editor.integration

import de.jcm.discordgamesdk.Core
import de.jcm.discordgamesdk.CreateParams
import de.jcm.discordgamesdk.LogLevel
import de.jcm.discordgamesdk.Result
import de.jcm.discordgamesdk.activity.Activity
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import turboedit.editor.events.EventSystem
import turboedit.editor.events.EventSystem.EventAction
import turboedit.editor.events.EventType
import turboedit.editor.ProjectManager.CURRENT_PROJECT
import java.time.Instant
import java.util.function.BiConsumer
import java.util.function.Consumer

class DiscordIntegration : Thread("DiscordIntegration") {
    override fun run() {
        super.run()

        CreateParams().use { params ->
            params.setClientID(APPLICATION_ID)
            params.setFlags(CreateParams.getDefaultFlags())

            Core(params).use { core ->
                core.setLogHook(LogLevel.VERBOSE, BiConsumer { level: LogLevel?, message: String? ->
                    when (level) {
                        LogLevel.INFO -> logger.info(message)
                        LogLevel.WARN -> logger.warn(message)
                        LogLevel.ERROR -> logger.error(message)
                        LogLevel.DEBUG -> logger.debug(message)
                        else -> {}
                    }
                })

                if (!core.isDiscordRunning) return

                Activity().use { activity ->
                    activity.timestamps().start = Instant.now()

                    EventSystem.registerListener(EventType.DISCORD_UPDATE_ACTIVITY, EventAction { dat: Any? ->
                        activity.details = CURRENT_PROJECT!!.name
                        activity.state = "Editing"
                        activity.assets().largeImage = "app_icon"
                        activity.assets().largeText = "TurboEdit"

                        core.activityManager().updateActivity(activity, Consumer { result: Result? ->
                            if (result != Result.OK) {
                                logger.error("Couldn't create discord activity")
                            }
                        })
                    })

                    EventSystem.callEvent(EventType.DISCORD_UPDATE_ACTIVITY)
                }
                while (true) {
                    core.runCallbacks()

                    try {
                        sleep(1000)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    companion object {
        private const val APPLICATION_ID = 1428707164876312607L
        private val logger: Logger = LoggerFactory.getLogger(DiscordIntegration::class.java)
    }
}
