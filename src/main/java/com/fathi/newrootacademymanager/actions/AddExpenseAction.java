package com.fathi.newrootacademymanager.actions;

import com.fathi.newrootacademymanager.models.Expense;
import com.fathi.newrootacademymanager.models.Teacher;
import com.fathi.newrootacademymanager.services.CRUDService;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

import java.math.BigDecimal;

public class AddExpenseAction extends FontIcon {
    public AddExpenseAction(BigDecimal amount, String details) {
        this.setIconLiteral("far-money-bill-alt");
        this.setIconSize(24);
        this.setIconColor(Color.valueOf("#00E676"));
        this.setCursor(Cursor.HAND);
        this.setOnMouseClicked((MouseEvent event) -> {
            CRUDService.create(new Expense(amount, details));
        });
    }

    public AddExpenseAction(BigDecimal amount, String details, Teacher teacher) {
        this.setIconLiteral("far-money-bill-alt");
        this.setIconSize(24);
        this.setIconColor(Color.valueOf("#00E676"));
        this.setCursor(Cursor.HAND);
        this.setOnMouseClicked((MouseEvent event) -> {
            CRUDService.create(new Expense(amount, details, teacher));
        });
    }
}
