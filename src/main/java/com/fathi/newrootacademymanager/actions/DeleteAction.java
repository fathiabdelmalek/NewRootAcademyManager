package com.fathi.newrootacademymanager.actions;

import com.fathi.newrootacademymanager.services.CRUDService;
import javafx.scene.Cursor;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

public class DeleteAction<T> extends FontIcon {
    public DeleteAction(TableView<T> tableView, Class<T> entityClass) {
        this.setIconLiteral("fas-trash");
        this.setIconSize(24);
        this.setIconColor(Color.valueOf("#E61744"));
        this.setCursor(Cursor.HAND);

        this.setOnMouseClicked((MouseEvent event) -> {
            CRUDService.delete(tableView.getSelectionModel().getSelectedItem());
            tableView.setItems(CRUDService.readAll(entityClass));
        });
    }
}
