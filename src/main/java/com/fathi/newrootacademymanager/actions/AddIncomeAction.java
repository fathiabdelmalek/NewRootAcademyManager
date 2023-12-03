package com.fathi.newrootacademymanager.actions;

import com.fathi.newrootacademymanager.models.Income;
import com.fathi.newrootacademymanager.models.Student;
import com.fathi.newrootacademymanager.services.CRUDService;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

public class AddIncomeAction extends FontIcon {
    public AddIncomeAction(float amount, String details) {
        this.setIconLiteral("far-money-bill-alt");
        this.setIconSize(24);
        this.setIconColor(Color.valueOf("#00E676"));
        this.setCursor(Cursor.HAND);
        this.setOnMouseClicked((MouseEvent event) -> {
            CRUDService.create(new Income(amount, details));
        });
    }

    public AddIncomeAction(float amount, String details, Student student) {
        this.setIconLiteral("far-money-bill-alt");
        this.setIconSize(24);
        this.setIconColor(Color.valueOf("#00E676"));
        this.setCursor(Cursor.HAND);
        this.setOnMouseClicked((MouseEvent event) -> {
            CRUDService.create(new Income(amount, details, student));
        });
    }
}
