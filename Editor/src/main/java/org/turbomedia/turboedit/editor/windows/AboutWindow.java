package org.turbomedia.turboedit.editor.windows;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static org.turbomedia.turboedit.editor.Editor.ICON;
import static org.turbomedia.turboedit.editor.Editor.TITLE;
import static org.turbomedia.turboedit.editor.misc.Locale.GetText;

public class AboutWindow extends Stage {
    public AboutWindow() {
        getIcons().add(ICON);
        setTitle(TITLE + " - " + GetText("title.about"));
        setAlwaysOnTop(true);
        setResizable(false);

        int HEIGHT = 100;
        setMaxHeight(HEIGHT);
        setHeight(HEIGHT);

        int WIDTH = 190;
        setMaxWidth(WIDTH);
        setWidth(WIDTH);

        setScene(new Scene(getPane()));

        initModality(Modality.APPLICATION_MODAL);
        showAndWait();
    }

    private GridPane getPane() {
        var panel = new GridPane();

        panel.setVgap(5);
        panel.setHgap(5);
        panel.setAlignment(Pos.CENTER);

        var text1 = new Text(GetText("menubar.help.about", TITLE));
        text1.setTextAlignment(TextAlignment.CENTER);
        panel.add(text1, 0, 0, 1, 1);

        var text2 = new Text("(c) 2025 TurboMedia");
        text2.setTextAlignment(TextAlignment.CENTER);
        panel.add(text2, 0, 1, 1, 1);

        return panel;
    }
}
