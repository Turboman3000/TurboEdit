package org.turbomedia.turboedit.editor.misc;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import static org.turbomedia.turboedit.editor.misc.PreferencesFile.CURRENT_PREFERENCES;

public class Locale {
    private static final Logger logger = LoggerFactory.getLogger(Locale.class);
    private static final HashMap<String, String> TRANSLATIONS = new HashMap<>();

    public void LoadLocales() throws IOException {
        var lang = CURRENT_PREFERENCES.language;

        logger.info("Loading Translations for: {}", lang.toUpperCase());

        try {
            var stream = getClass().getResourceAsStream("/lang/" + lang + ".json");

            assert stream != null;
            var reader = new BufferedReader(new InputStreamReader(stream));
            var gson = new Gson().newJsonReader(reader);

            gson.beginObject();

            while (gson.hasNext()) {
                var key = gson.nextName();
                var value = gson.nextString();

                TRANSLATIONS.put(key, value);
            }

            gson.endObject();
            logger.info("Translations successfully loaded!");
        } catch (Exception e) {
            logger.error("{} = {}", e.getClass().getName(), e.getMessage());
        }
    }

    public static String GetText(String key) {
        if (TRANSLATIONS.containsKey(key)) {
            return TRANSLATIONS.get(key);
        } else {
            logger.warn("Translation for Key: {} not found!", key);
            return key;
        }
    }

    public static String GetText(String key, String... params) {
        if (TRANSLATIONS.containsKey(key)) {
            var text = TRANSLATIONS.get(key);

            for (var x = 0; x < params.length; x++) {
                text = text.replaceAll("%" + x, params[x]);
            }

            return text;
        } else {
            logger.warn("Translation for Key: {} not found!", key);
            return key;
        }
    }
}
