package org.turbomedia.turboedit.editor.windows.errors;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

import static org.turbomedia.turboedit.editor.Editor.ICON;
import static org.turbomedia.turboedit.editor.Editor.TITLE;
import static org.turbomedia.turboedit.editor.misc.Locale.GetText;

public class FileErrorWindow extends Stage {
    public FileErrorWindow(String title, String text, ArrayList<String> files) {
        var FONT_NAME = Font.getDefault().getName();

        setAlwaysOnTop(true);
        setTitle(TITLE + " - " + title);
        getIcons().add(ICON);
        setResizable(false);

        setWidth(800);

        var label = new Text(text);
        label.setFont(Font.font(FONT_NAME, FontWeight.MEDIUM, 14));

        var fileList = new ListView<String>();
        fileList.setEditable(false);

        for (var file : files) {
            fileList.getItems().add(file);
        }

        var closeButton = new Button(GetText("error.button.close"));
        closeButton.setCursor(Cursor.HAND);
        closeButton.setOnAction((event) -> close());

        var buttonBox = new HBox(closeButton);
        buttonBox.setSpacing(6);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        var box = new VBox(label, fileList, buttonBox);
        box.setSpacing(6);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(15));

        setScene(new Scene(box));

        centerOnScreen();
        initModality(Modality.APPLICATION_MODAL);
        showAndWait();
    }
}
