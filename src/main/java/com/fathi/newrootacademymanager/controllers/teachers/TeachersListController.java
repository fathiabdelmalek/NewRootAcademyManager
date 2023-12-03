package com.fathi.newrootacademymanager.controllers.teachers;

import com.fathi.newrootacademymanager.helpers.ViewFactory;
import com.fathi.newrootacademymanager.models.StudentView;
import com.fathi.newrootacademymanager.models.Teacher;
import com.fathi.newrootacademymanager.services.CRUDService;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class TeachersListController {
    @FXML
    public TextField searchText;
    @FXML
    public TableView<Teacher> tableView;
    @FXML
    public TableColumn<Teacher, String> actionsColumn;

    @FXML
    protected void initialize() {
        loadData();
    }

    public void addAction() {
//        ViewFactory.createAddView("/com/fathi/newrootacademymanager/views/students/add-teacher-view.fxml", "Add Teacher");
        refreshTable();
    }

    public void searchAction(KeyEvent keyEvent) {
//        String name = searchText.getText();
    }

    private void loadData() {
        refreshTable();
    }

    private void refreshTable() {
        tableView.setItems(CRUDService.readAll(Teacher.class));
    }
}
