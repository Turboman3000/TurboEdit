package de.turboman.edit.editor.panes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

import static de.turboman.edit.editor.PreferencesFile.CURRENT_PREFERENCES;

public class PreferencesPane extends TabPane {

    public PreferencesPane() {
        setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        getTabs().add(appearanceTab());
        getTabs().add(renderingTab());
        getTabs().add(performanceTab());
        getTabs().add(socialMediaTab());
    }

    private Tab appearanceTab() {
        var languageSelection = new ChoiceBox<Language>();
        languageSelection.getItems().add(new Language("English (US)", "en_us"));
        languageSelection.getItems().add(new Language("German", "de"));
        languageSelection.getItems().add(new Language("Swiss German", "gsw"));
        languageSelection.getItems().add(new Language("French", "fr"));

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
                throw new RuntimeException(e);
            }
        });

        var languageBox = new HBox(new Label("Language"), languageSelection);
        languageBox.setAlignment(Pos.CENTER);
        languageBox.setSpacing(10);

        var colorModeSelection = new ChoiceBox<ColorModeValue>();
        colorModeSelection.getItems().add(new ColorModeValue("Follow System", ColorMode.SYSTEM));
        colorModeSelection.getItems().add(new ColorModeValue("Dark", ColorMode.DARK));
        colorModeSelection.getItems().add(new ColorModeValue("Light", ColorMode.LIGHT));

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
                throw new RuntimeException(e);
            }
        });

        var colorModeBox = new HBox(new Label("Color Mode"), colorModeSelection);
        colorModeBox.setAlignment(Pos.CENTER);
        colorModeBox.setSpacing(10);

        var box = new VBox(languageBox, colorModeBox);
        box.setPadding(new Insets(10));
        box.setSpacing(10);

        return new Tab("Appearance", box);
    }

    private Tab renderingTab() {
        var box = new VBox();
        box.setPadding(new Insets(10));
        box.setSpacing(10);

        return new Tab("Rendering", box);
    }

    private Tab performanceTab() {
        var box = new VBox();
        box.setPadding(new Insets(10));
        box.setSpacing(10);

        return new Tab("Performance", box);
    }

    private Tab socialMediaTab() {
        var box = new VBox();
        box.setPadding(new Insets(10));
        box.setSpacing(10);

        return new Tab("Social Media", box);
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
