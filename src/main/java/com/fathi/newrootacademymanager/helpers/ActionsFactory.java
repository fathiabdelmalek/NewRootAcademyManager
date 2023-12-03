package com.fathi.newrootacademymanager.helpers;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;

public class ActionsFactory <T> implements Callback<TableColumn<T, String>, TableCell<T, String>> {
    @Override
    public TableCell<T, String> call(TableColumn<T, String> tStringTableColumn) {
        return null;
    }

    public ActionsFactory(FontIcon[] icons) {
        this.icons = List.of(icons);
    }

    private List<FontIcon> icons;
}
