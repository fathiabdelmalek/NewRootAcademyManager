package com.fathi.newrootacademymanager.controllers.students;

import com.fathi.newrootacademymanager.helpers.ViewFactory;
import com.fathi.newrootacademymanager.models.StudentView;
import com.fathi.newrootacademymanager.services.CRUDService;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class StudentsListController {
    @FXML
    private TextField searchText;
    @FXML
    private TableView<StudentView> tableView;
    @FXML
    private TableColumn<StudentView, String> actionsColumn;

    @FXML
    protected void initialize() {
        loadData();
    }

    public void addAction() {
        ViewFactory.createAddView("/com/fathi/newrootacademymanager/views/students/add-student-view.fxml", "Add Student");
        refreshTable();
    }

    public void searchAction(KeyEvent keyEvent) {
//        String name = searchText.getText();
    }

    private void loadData() {
        refreshTable();
    }

    private void refreshTable() {
        tableView.setItems(CRUDService.readAll(StudentView.class));
    }
}
