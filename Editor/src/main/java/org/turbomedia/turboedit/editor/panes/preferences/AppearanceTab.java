package org.turbomedia.turboedit.editor.panes.preferences;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.turbomedia.turboedit.editor.misc.Locale.GetText;
import static org.turbomedia.turboedit.editor.misc.PreferencesFile.CURRENT_PREFERENCES;

public class AppearanceTab extends Tab {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public AppearanceTab() {
        var languageSelection = new ChoiceBox<Language>();
        languageSelection.getItems().add(new Language(GetText("language.en_us"), "en_us"));
      /*  languageSelection.getItems().add(new Language(GetText("language.de"), "de"));
        languageSelection.getItems().add(new Language(GetText("language.gsw"), "gsw"));
        languageSelection.getItems().add(new Language(GetText("language.fr"), "fr"));*/

        for (var language : languageSelection.getItems()) {
            if (language.value.equals(CURRENT_PREFERENCES.language())) {
                languageSelection.setValue(language);
                break;
            }
        }

        languageSelection.setOnAction(event -> {
            try {
                CURRENT_PREFERENCES.language(languageSelection.getValue().value);
            } catch (IOException | InterruptedException e) {
                logger.error("{} = {}", e.getClass().getName(), e.getMessage());
            }
        });

        var languageBox = new HBox(new Label(GetText("preferences.appearance.choice.language.label")), languageSelection);
        languageBox.setAlignment(Pos.CENTER);
        languageBox.setSpacing(10);

        var colorModeSelection = new ChoiceBox<ColorModeValue>();
        colorModeSelection.getItems().add(new ColorModeValue(GetText("preferences.appearance.choice.color_mode.system"), ColorMode.SYSTEM));
        colorModeSelection.getItems().add(new ColorModeValue(GetText("preferences.appearance.choice.color_mode.dark"), ColorMode.DARK));
        colorModeSelection.getItems().add(new ColorModeValue(GetText("preferences.appearance.choice.color_mode.light"), ColorMode.LIGHT));

        for (var mode : colorModeSelection.getItems()) {
            if (mode.value.ordinal() == CURRENT_PREFERENCES.colorMode()) {
                colorModeSelection.setValue(mode);
                break;
            }
        }

        colorModeSelection.setOnAction(event -> {
            try {
                CURRENT_PREFERENCES.colorMode(colorModeSelection.getValue().value.ordinal());
            } catch (IOException | InterruptedException e) {
                logger.error("{} = {}", e.getClass().getName(), e.getMessage());
            }
        });

        var colorModeBox = new HBox(new Label(GetText("preferences.appearance.choice.color_mode.label")), colorModeSelection);
        colorModeBox.setAlignment(Pos.CENTER);
        colorModeBox.setSpacing(10);

        var box = new VBox(languageBox, colorModeBox);
        box.setPadding(new Insets(10));
        box.setSpacing(10);

        setText(GetText("preferences.appearance.tab"));
        setContent(box);
    }

    private record Language(String label, String value) {
        @Override
        public String toString() {
            return label;
        }
    }

    private record ColorModeValue(String label, ColorMode value) {
        @Override
        public String toString() {
            return label;
        }
    }

    private enum ColorMode {
        SYSTEM,
        DARK,
        LIGHT
    }
}
