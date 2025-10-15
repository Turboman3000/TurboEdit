package org.turbomedia.turboedit.editor.windows;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static org.turbomedia.turboedit.editor.Editor.ICON;
import static org.turbomedia.turboedit.editor.Editor.TITLE;

public class SimpleMessageBox extends Stage {
    public SimpleMessageBox(String title, String message) {
        var messageText = new Text(message);
        var okButton = new Button("Okay");
        okButton.setCursor(Cursor.HAND);
        okButton.setOnAction((event) -> close());

        var box = new VBox(messageText, okButton);

        box.setAlignment(Pos.CENTER_RIGHT);
        box.setSpacing(6);
        box.setPadding(new Insets(15));

        setResizable(false);
        setScene(new Scene(box));
        initModality(Modality.APPLICATION_MODAL);
        setAlwaysOnTop(true);
        setTitle(TITLE + " - " + title);
        getIcons().add(ICON);
        centerOnScreen();
        show();
    }
}
