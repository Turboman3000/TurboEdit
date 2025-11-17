package turboedit.editor.panes.file_browser

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.geometry.Side
import javafx.scene.Cursor
import javafx.scene.control.Button
import javafx.scene.control.MenuButton
import javafx.scene.control.MenuItem
import javafx.scene.control.Tooltip
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import org.kordamp.ikonli.fluentui.FluentUiFilledAL
import org.kordamp.ikonli.fluentui.FluentUiFilledMZ
import org.kordamp.ikonli.javafx.FontIcon
import turboedit.editor.events.EventSystem.EventAction
import turboedit.editor.events.EventSystem.registerListener
import turboedit.editor.events.EventType
import turboedit.editor.events.ThemeChangedEventData
import turboedit.editor.misc.Locale
import turboedit.editor.misc.StyleManager

class FileActions : VBox() {
    init {
        registerListener(EventType.THEME_CHANGED, EventAction { dat: Any? ->
            val data = dat as ThemeChangedEventData?
            val color = when (data!!.theme) {
                StyleManager.Theme.DARK -> Color.color(1.0, 1.0, 1.0, 0.05)
                StyleManager.Theme.LIGHT -> Color.color(0.0, 0.0, 0.0, 0.05)
                else -> {}
            }

            val background = Background(BackgroundFill(color as Paint?, CornerRadii(10.0), Insets.EMPTY))
            setBackground(background)
        })

        val renameButton = Button()
        renameButton.tooltip = Tooltip(Locale.getText("file_browser.action.rename"))
        renameButton.cursor = Cursor.HAND
        renameButton.isDisable = true
        renameButton.graphic = FontIcon.of(FluentUiFilledMZ.RENAME_24)

        val deleteButton = Button()
        deleteButton.tooltip = Tooltip(Locale.getText("file_browser.action.delete"))
        deleteButton.cursor = Cursor.HAND
        deleteButton.isDisable = true
        deleteButton.graphic = FontIcon.of(FluentUiFilledAL.DELETE_24)

        val reloadButton = Button()
        reloadButton.tooltip = Tooltip(Locale.getText("file_browser.action.reload"))
        reloadButton.cursor = Cursor.HAND
        reloadButton.graphic = FontIcon.of(FluentUiFilledAL.ARROW_CLOCKWISE_24)

        val addFolderButton = Button()
        addFolderButton.tooltip = Tooltip(Locale.getText("file_browser.action.add_folder"))
        addFolderButton.cursor = Cursor.HAND
        addFolderButton.graphic = FontIcon.of(FluentUiFilledAL.FOLDER_ADD_24)

        val addButton = Button()
        addButton.tooltip = Tooltip(Locale.getText("file_browser.action.add"))
        addButton.cursor = Cursor.HAND
        addButton.graphic = FontIcon.of(FluentUiFilledAL.DOCUMENT_ADD_24)

        val sortButton = MenuButton()
        sortButton.tooltip = Tooltip(Locale.getText("file_browser.action.sort"))
        sortButton.cursor = Cursor.HAND
        sortButton.graphic = FontIcon.of(FluentUiFilledAL.ARROW_SORT_24)
        sortButton.popupSide = Side.TOP

        run {
            val sort1 = MenuItem(Locale.getText("file_browser.action.sort.name_asc"))
            val sort2 = MenuItem(Locale.getText("file_browser.action.sort.name_desc"))

            val sort3 = MenuItem(Locale.getText("file_browser.action.sort.type_asc"))
            val sort4 = MenuItem(Locale.getText("file_browser.action.sort.type_desc"))
            sortButton.items.addAll(sort1, sort2, sort3, sort4)
        }

        setVgrow(this, Priority.ALWAYS)

        children.addAll(renameButton, deleteButton, reloadButton, addButton, addFolderButton, sortButton)
        alignment = Pos.BOTTOM_CENTER
        maxHeight = Double.Companion.MAX_VALUE
        spacing = 6.0
        padding = Insets(6.0)
    }
}
