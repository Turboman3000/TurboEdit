package turboedit.editor.misc

import com.google.gson.Gson
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import turboedit.editor.misc.PreferencesFile.CURRENT_PREFERENCES
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.Locale

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class Locale {
    @Throws(IOException::class)
    fun loadLocales() {
        val lang: String? = CURRENT_PREFERENCES!!.language

        logger.info("Loading Translations for: {}", lang!!.uppercase(Locale.getDefault()))

        try {
            val stream: InputStream? = checkNotNull(javaClass.getResourceAsStream("/lang/$lang.json"))

            val reader = BufferedReader(InputStreamReader(stream))
            val gson = Gson().newJsonReader(reader)

            gson.beginObject()

            while (gson.hasNext()) {
                val key = gson.nextName()
                val value = gson.nextString()

                TRANSLATIONS.put(key, value!!)
            }

            gson.endObject()
            logger.info("Translations successfully loaded!")
        } catch (e: Exception) {
            logger.error("{} = {}", e.javaClass.getName(), e.message)
        }
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(turboedit.editor.misc.Locale::class.java)
        private val TRANSLATIONS = HashMap<String?, String>()

        @JvmStatic
        fun getText(key: String): String {
            if (TRANSLATIONS.containsKey(key)) {
                return TRANSLATIONS.get(key)!!
            } else {
                logger.warn("Translation for Key: {} not found!", key)
                return key
            }
        }

        @JvmStatic
        fun getText(key: String, vararg params: String): String {
            if (TRANSLATIONS.containsKey(key)) {
                var text: String = TRANSLATIONS.get(key)!!

                for (x in params.indices) {
                    text = text.replace(("%$x").toRegex(), params[x])
                }

                return text
            } else {
                logger.warn("Translation for Key: {} not found!", key)
                return key
            }
        }
    }
}
