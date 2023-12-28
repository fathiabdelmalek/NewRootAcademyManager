package com.fathi.newrootacademymanager.controllers.grades;

import com.fathi.newrootacademymanager.helpers.enums.Level;
import com.fathi.newrootacademymanager.models.Grade;
import com.fathi.newrootacademymanager.services.CRUDService;
import com.fathi.newrootacademymanager.services.LoggingService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class GradesViewController {
    @FXML
    public TextField searchText;
    @FXML
    public TableView<Grade> tableView;
    @FXML
    private ComboBox<Level> levelChoice;
    @FXML
    private TextField yearText;
    private int id;

    @FXML
    void initialize() {
        levelChoice.setItems(FXCollections.observableArrayList(Level.values()));
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                id = newSelection.getId();
                levelChoice.getSelectionModel().select(newSelection.getLevel());
                yearText.setText(String.valueOf(newSelection.getYearOfGrade()));
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
                        String.valueOf(predicate.getYearOfGrade()).contains(key);
            });
        });
        tableView.setItems(filter);
    }

    @FXML
    void insertAction() {
        if (levelChoice.getSelectionModel().isEmpty() ||
                yearText.getText().isEmpty())
            LoggingService.error("You should insert all required fields");
        else {
            Grade grade = new Grade(levelChoice.getValue(), Integer.parseInt(yearText.getText()));
            CRUDService.create(grade);
            refreshTable();
            LoggingService.add("Grade " + grade + " have been added");
        }
    }

    @FXML
    void updateAction() {
        if (levelChoice.getSelectionModel().isEmpty() ||
                yearText.getText().isEmpty())
            LoggingService.error("You should insert all required fields");
        else {
            Grade grade = CRUDService.readById(Grade.class, id);
            String oldGrade = grade.toString();
            grade.setLevel(levelChoice.getValue());
            grade.setYearOfGrade(Integer.parseInt(yearText.getText()));
            CRUDService.update(grade);
            refreshTable();
            LoggingService.update("Grade " + oldGrade + "have been change to " + grade);
        }
    }

    @FXML
    void deleteAction() {
        Grade grade = CRUDService.readById(Grade.class, id);
        CRUDService.delete(grade);
        refreshTable();
        LoggingService.delete("Grade " + grade + " have been deleted");
    }

    @FXML
    void clearAction() {
        levelChoice.getSelectionModel().clearSelection();
        yearText.clear();
    }

    private void refreshTable() {
        tableView.setItems(CRUDService.readAll(Grade.class));
    }
}
