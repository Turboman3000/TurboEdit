package turboedit.editor.panes.preferences

import atlantafx.base.controls.Spacer
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.control.CheckBox
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Label
import javafx.scene.control.Tab
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import turboedit.editor.misc.Locale
import turboedit.editor.misc.PreferencesFile.CURRENT_PREFERENCES
import java.io.IOException

class GeneralTab : Tab() {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    init {
        val mainBox = VBox()

        run {
            val languageSelection = ChoiceBox<Language>()
            languageSelection.getItems().addAll(
                Language(Locale.getText("language.en_us"), "en_us") //   new Language(GetText("language.de"), "de"),
                //   new Language(GetText("language.gsw"), "gsw"),
                //   new Language(GetText("language.fr"), "fr")
            )

            for (language in languageSelection.getItems()) {
                if (language.value == CURRENT_PREFERENCES!!.language()) {
                    languageSelection.value = language
                    break
                }
            }

            languageSelection.onAction = EventHandler { event: ActionEvent? ->
                try {
                    CURRENT_PREFERENCES!!.language(languageSelection.getValue().value)
                } catch (e: IOException) {
                    logger.error("{} = {}", e.javaClass.getName(), e.message)
                } catch (e: InterruptedException) {
                    logger.error("{} = {}", e.javaClass.getName(), e.message)
                }
            }

            val languageLabel = Label(Locale.getText("preferences.general.choice.language.label"))
            languageLabel.labelFor = languageSelection

            val languageBox = VBox(
                languageLabel,
                languageSelection
            )
            languageBox.alignment = Pos.CENTER_LEFT
            languageBox.spacing = 10.0

            val colorModeSelection: ChoiceBox<ColorModeValue> = ChoiceBox<ColorModeValue>()
            colorModeSelection.getItems().addAll(
                ColorModeValue(Locale.getText("preferences.general.choice.color_mode.system"), ColorMode.SYSTEM),
                ColorModeValue(Locale.getText("preferences.general.choice.color_mode.dark"), ColorMode.DARK),
                ColorModeValue(Locale.getText("preferences.general.choice.color_mode.light"), ColorMode.LIGHT)
            )

            for (mode in colorModeSelection.getItems()) {
                if (mode.value!!.ordinal == CURRENT_PREFERENCES!!.colorMode()) {
                    colorModeSelection.value = mode
                    break
                }
            }
            colorModeSelection.onAction = EventHandler { event: ActionEvent? ->
                try {
                    CURRENT_PREFERENCES!!.colorMode(colorModeSelection.getValue().value!!.ordinal)
                } catch (e: IOException) {
                    logger.error("{} = {}", e.javaClass.getName(), e.message)
                } catch (e: InterruptedException) {
                    logger.error("{} = {}", e.javaClass.getName(), e.message)
                }
            }

            val colorBoxLabel = Label(Locale.getText("preferences.general.choice.color_mode.label"))
            colorBoxLabel.labelFor = colorModeSelection

            val colorModeBox = VBox(
                colorBoxLabel,
                colorModeSelection
            )
            colorModeBox.alignment = Pos.CENTER_LEFT
            colorModeBox.spacing = 10.0

            val box = HBox(Spacer(), languageBox, colorModeBox, Spacer())
            box.spacing = 10.0
            mainBox.children.add(box)
        }

        run {
            val enableDiscordRPC = CheckBox("Enabled Discord RPC")
            enableDiscordRPC.cursor = Cursor.HAND
            mainBox.children.add(enableDiscordRPC)
        }

        mainBox.padding = Insets(15.0)
        mainBox.spacing = 10.0
        mainBox.alignment = Pos.TOP_CENTER

        text = Locale.getText("preferences.general.tab")
        content = mainBox
    }

    @JvmRecord
    private data class Language(val label: String, val value: String?) {
        override fun toString(): String {
            return label
        }
    }

    @JvmRecord
    private data class ColorModeValue(val label: String, val value: ColorMode?) {
        override fun toString(): String {
            return label
        }
    }

    private enum class ColorMode {
        SYSTEM,
        DARK,
        LIGHT
    }
}
