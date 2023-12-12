package com.fathi.newrootacademymanager.controllers.grades;

import com.fathi.newrootacademymanager.helpers.enums.Level;
import com.fathi.newrootacademymanager.models.Grade;
import com.fathi.newrootacademymanager.services.CRUDService;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class GradesViewController {
    @FXML
    public TextField searchGradeText;
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
            id = newSelection.getId();
            levelText.setText(newSelection.getLevel().toString());
            yearText.setText(String.valueOf(newSelection.getYear()));
        });
        refreshTable();
    }

    @FXML
    void searchAction() {
        FilteredList<Grade> filter = new FilteredList<>(tableView.getItems(), e -> true);
        searchGradeText.textProperty().addListener((Observable, oldValue, newValue) -> {
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
            System.out.println("You should insert all required data");
        else {
            CRUDService.create(new Grade(Level.valueOf(levelText.getText()), Integer.parseInt(yearText.getText())));
            refreshTable();
        }
    }

    @FXML
    void updateAction() {
        if (levelText.getText().isEmpty() ||
                yearText.getText().isEmpty())
            System.out.println("You should insert all required data");
        else {
            Grade grade = CRUDService.readById(Grade.class, id);
            assert grade != null;
            grade.setLevel(Level.valueOf(levelText.getText()));
            grade.setYear(Integer.parseInt(yearText.getText()));
            CRUDService.update(grade);
            refreshTable();
        }
    }

    @FXML
    void deleteAction() {
        CRUDService.delete(CRUDService.readById(Grade.class, id));
        refreshTable();
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
