package com.fathi.newrootacademymanager.controllers.grades;

import com.fathi.newrootacademymanager.helpers.ViewFactory;
import com.fathi.newrootacademymanager.models.Grade;
import com.fathi.newrootacademymanager.models.StudentView;
import com.fathi.newrootacademymanager.services.CRUDService;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;

public class GradesListController {
    @FXML
    public TableView<Grade> tableView;

    @FXML
    protected void initialize() {
        loadData();
    }

    public void addAction() {
//        ViewFactory.createAddView("/com/fathi/newrootacademymanager/views/students/add-grade-view.fxml", "Add Grade");
        refreshTable();
    }

    private void loadData() {
        refreshTable();
    }

    private void refreshTable() {
        tableView.setItems(CRUDService.readAll(Grade.class));
    }
}
