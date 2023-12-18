package com.fathi.newrootacademymanager.controllers.grades;

import com.fathi.newrootacademymanager.helpers.enums.Level;
import com.fathi.newrootacademymanager.models.Grade;
import com.fathi.newrootacademymanager.services.CRUDService;
import com.fathi.newrootacademymanager.services.LoggingService;
import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class GradesViewController {
    @FXML
    public TextField searchText;
    @FXML
    public TableView<Grade> tableView;
    @FXML
    private TextField levelText;
    @FXML
    private TextField yearText;
    private int id;

    @FXML
    void initialize() {
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                id = newSelection.getId();
                levelText.setText(newSelection.getLevel().toString());
                yearText.setText(String.valueOf(newSelection.getYear()));
            }
        });
        refreshTable();
    }

    @FXML
    void searchAction() {
        FilteredList<Grade> filter = new FilteredList<>(tableView.getItems(), e -> true);
        searchText.textProperty().addListener((Observable, oldValue, newValue) -> {
            filter.setPredicate(predicate -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String key = newValue.toLowerCase();
                return predicate.getLevel().toString().toLowerCase().contains(key) ||
                        String.valueOf(predicate.getYear()).contains(key);
            });
        });
        tableView.setItems(filter);
    }

    @FXML
    void insertAction() {
        if (levelText.getText().isEmpty() ||
                yearText.getText().isEmpty())
            LoggingService.error("You should insert all required fields");
        else {
            Grade grade = new Grade(Level.valueOf(levelText.getText()), Integer.parseInt(yearText.getText()));
            CRUDService.create(grade);
            refreshTable();
            LoggingService.add(grade + " have been added");
        }
    }

    @FXML
    void updateAction() {
        if (levelText.getText().isEmpty() ||
                yearText.getText().isEmpty())
            LoggingService.error("You should insert all required fields");
        else {
            Grade grade = CRUDService.readById(Grade.class, id);
            String oldGrade = grade.toString();
            grade.setLevel(Level.valueOf(levelText.getText()));
            grade.setYear(Integer.parseInt(yearText.getText()));
            CRUDService.update(grade);
            refreshTable();
            LoggingService.update(oldGrade + "have been change to " + grade);
        }
    }

    @FXML
    void deleteAction() {
        Grade grade = CRUDService.readById(Grade.class, id);
        CRUDService.delete(grade);
        refreshTable();
        LoggingService.delete(grade + " have been deleted");
    }

    @FXML
    void clearAction() {
        levelText.clear();
        yearText.clear();
    }

    private void refreshTable() {
        tableView.setItems(CRUDService.readAll(Grade.class));
    }
}
