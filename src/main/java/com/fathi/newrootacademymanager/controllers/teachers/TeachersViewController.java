package com.fathi.newrootacademymanager.controllers.teachers;

import com.fathi.newrootacademymanager.helpers.enums.Sex;
import com.fathi.newrootacademymanager.models.Grade;
import com.fathi.newrootacademymanager.models.Student;
import com.fathi.newrootacademymanager.models.StudentView;
import com.fathi.newrootacademymanager.models.Teacher;
import com.fathi.newrootacademymanager.services.CRUDService;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

public class TeachersViewController {
    @FXML
    private TextField searchText;
    @FXML
    private TableView<Teacher> tableView;
    @FXML
    private TextField firstNameText;
    @FXML
    private TextField lastNameText;
    @FXML
    private TextField phoneNumberText;

    private int id;

    public void initialize() {
        loadUI();
    }

    public void searchAction(KeyEvent keyEvent) {
        FilteredList<Teacher> filter = new FilteredList<>(tableView.getItems(), e -> true);
        searchText.textProperty().addListener((Observable, oldValue, newValue) -> {
            filter.setPredicate(predicate -> {
                if (newValue == null || newValue.isEmpty())
                    return true;
                String key = newValue.toLowerCase();
                return predicate.getFirstName().toLowerCase().contains(key) ||
                        predicate.getLastName().toLowerCase().contains(key) ||
                        predicate.getPhoneNumber().contains(key);
            });
        });
        SortedList<Teacher> sortedList = new SortedList<>(filter);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);
    }

    public void insertAction(ActionEvent actionEvent) {
        if (firstNameText.getText().isEmpty() ||
                lastNameText.getText().isEmpty() ||
                phoneNumberText.getText().isEmpty()) {
            System.out.println("You should insert all required data");
        } else {
            CRUDService.create(
                    new Teacher(
                            firstNameText.getText(),
                            lastNameText.getText(),
                            phoneNumberText.getText()
                    )
            );
            refreshTable();
        }
    }

    public void updateAction(ActionEvent actionEvent) {
        Teacher teacher = CRUDService.readById(Teacher.class, id);
        assert teacher != null;
        teacher.setFirstName(firstNameText.getText());
        teacher.setLastName(lastNameText.getText());
        teacher.setPhoneNumber(phoneNumberText.getText());
        CRUDService.update(teacher);
        refreshTable();
    }

    public void deleteAction(ActionEvent actionEvent) {
        Teacher teacher = CRUDService.readById(Teacher.class, id);
        CRUDService.delete(teacher);
        refreshTable();
    }

    public void clearAction(ActionEvent actionEvent) {
        firstNameText.clear();
        lastNameText.clear();
        phoneNumberText.clear();
    }

    private void loadUI() {
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            id = newSelection.getId();
            firstNameText.setText(newSelection.getFirstName());
            lastNameText.setText(newSelection.getLastName());
            phoneNumberText.setText(newSelection.getPhoneNumber());
        });
        refreshTable();
    }

    private void refreshTable() {
        tableView.setItems(CRUDService.readAll(Teacher.class));
    }
}
