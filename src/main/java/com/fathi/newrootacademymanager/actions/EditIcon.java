package com.fathi.newrootacademymanager.actions;

import com.fathi.newrootacademymanager.helpers.ViewFactory;
import com.fathi.newrootacademymanager.services.CRUDService;
import javafx.scene.Cursor;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

public class EditIcon<T> extends FontIcon {
    public EditIcon(TableView<T> tableView, Class<T> entityClass, String viewPath, String title) {
        this.setIconLiteral("fas-pen");
        this.setIconSize(24);
        this.setIconColor(Color.valueOf("#0076E6"));
        this.setCursor(Cursor.HAND);

        this.setOnMouseClicked((MouseEvent event) -> {
            T entity = tableView.getSelectionModel().getSelectedItem();
            ViewFactory.createEditView(entityClass, viewPath, title);
            tableView.setItems(CRUDService.readAll(entityClass));
        });
    }
}
