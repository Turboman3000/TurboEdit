package org.turbomedia.turboedit.editor.integration;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.LogLevel;
import de.jcm.discordgamesdk.Result;
import de.jcm.discordgamesdk.activity.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.turbomedia.turboedit.editor.events.EventSystem;
import org.turbomedia.turboedit.editor.events.EventType;

import java.time.Instant;

import static org.turbomedia.turboedit.editor.ProjectManager.CURRENT_PROJECT;

public class DiscordIntegration extends Thread {
    private final static long APPLICATION_ID = 1428707164876312607L;
    private final static Logger logger = LoggerFactory.getLogger(DiscordIntegration.class);

    public DiscordIntegration() {
        super("DiscordIntegration");
    }

    @Override
    public void run() {
        super.run();

        try (var params = new CreateParams()) {
            params.setClientID(APPLICATION_ID);
            params.setFlags(CreateParams.getDefaultFlags());

            try (var core = new Core(params)) {
                core.setLogHook(LogLevel.VERBOSE, (level, message) -> {
                    switch (level) {
                        case INFO -> logger.info(message);
                        case WARN -> logger.warn(message);
                        case ERROR -> logger.error(message);
                        case DEBUG -> logger.debug(message);
                    }
                });

                if (!core.isDiscordRunning()) return;

                try (var activity = new Activity()) {
                    activity.timestamps().setStart(Instant.now());

                    EventSystem.RegisterListener(EventType.DISCORD_UPDATE_ACTIVITY, (dat) -> {
                        activity.setDetails(CURRENT_PROJECT.name());
                        activity.setState("Editing");
                        activity.assets().setLargeImage("app_icon");
                        activity.assets().setLargeText("TurboEdit");

                        core.activityManager().updateActivity(activity, (result) -> {
                            if (result != Result.OK) {
                                logger.error("Couldn't create discord activity");
                            }
                        });
                    });

                    EventSystem.CallEvent(EventType.DISCORD_UPDATE_ACTIVITY);
                }

                while (true) {
                    core.runCallbacks();

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
