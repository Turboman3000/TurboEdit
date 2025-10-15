package org.turbomedia.turboedit.editor.panes.preferences;

import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.turbomedia.turboedit.editor.misc.Locale.GetText;

public class PerformanceTab extends Tab {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public PerformanceTab() {
        var box = new VBox();
        box.setPadding(new Insets(10));
        box.setSpacing(10);

        setText(GetText("preferences.performance.tab"));
        setContent(box);
    }
}
