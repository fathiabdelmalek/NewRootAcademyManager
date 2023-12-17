package com.fathi.newrootacademymanager.controllers.expenses;

import com.fathi.newrootacademymanager.models.*;
import com.fathi.newrootacademymanager.services.CRUDService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ExpensesViewController {
    @FXML
    private TextField searchText;
    @FXML
    private TableView<ExpenseView> tableView;
    @FXML
    private TextField amountText;
    @FXML
    private TextField detailsText;
    @FXML
    private ComboBox<Teacher> teacherChoice;
    private int id;

    @FXML
    void initialize() {
        Platform.runLater(() -> searchText.requestFocus());

        teacherChoice.setItems(FXCollections.observableList(CRUDService.readAll(Teacher.class)));
        teacherChoice.getItems().addFirst(null);
        teacherChoice.setConverter(new StringConverter<>() {
            @Override
            public String toString(Teacher teacher) {
                if (teacher == null) return null;
                return teacher.getFirstName() + " " + teacher.getLastName();
            }

            @Override
            public Teacher fromString(String string) {
                return null;
            }
        });
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                id = newSelection.getId();
                amountText.setText(String.valueOf(newSelection.getAmount()));
                detailsText.setText(newSelection.getDetails());
                String selectedStudent = newSelection.getTeacherName();
                for (Teacher teacher : teacherChoice.getItems()) {
                    if (teacher == null) continue;
                    String studentName = teacher.getFirstName() + " " + teacher.getLastName();
                    if (studentName.equalsIgnoreCase(selectedStudent)) {
                        teacherChoice.setValue(teacher);
                        break;
                    }
                }
            }
        });
        refreshTable();
    }

    @FXML
    void searchAction(KeyEvent keyEvent) {
        FilteredList<ExpenseView> filter = new FilteredList<>(tableView.getItems(), e -> true);
        searchText.textProperty().addListener((Observable, oldValue, newValue) -> {
            filter.setPredicate(predicate -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String key = newValue.toLowerCase();
                if (predicate.getTeacherName() != null)
                    return String.valueOf(predicate.getAmount()).contains(key) ||
                            predicate.getTime().toLowerCase().contains(key) ||
                            predicate.getTeacherName().toLowerCase().contains(key);
                return String.valueOf(predicate.getAmount()).contains(key) ||
                        predicate.getTime().toLowerCase().contains(key);
            });
        });
        tableView.setItems(filter);
    }

    @FXML
    void insertAction(ActionEvent actionEvent) {
        if (amountText.getText().isEmpty() || detailsText.getText().isEmpty())
            System.out.println("You should insert all required data");
        else {
            CRUDService.create(
                    new Expense(
                            new BigDecimal(amountText.getText()),
                            detailsText.getText(),
                            teacherChoice.getValue()
                    )
            );
            refreshTable();
        }
    }

    @FXML
    void updateAction(ActionEvent actionEvent) {
        if (amountText.getText().isEmpty() || detailsText.getText().isEmpty())
            System.out.println("You should insert all required data");
        else {
            Expense expense = CRUDService.readById(Expense.class, id);
            expense.setAmount(new BigDecimal(amountText.getText()));
            expense.setDetails(detailsText.getText());
            expense.setTeacher(teacherChoice.getValue());
            expense.setUpdateTime(LocalDateTime.now());
            CRUDService.update(expense);
            refreshTable();
        }
    }

    @FXML
    void deleteAction(ActionEvent actionEvent) {
        CRUDService.delete(CRUDService.readById(Expense.class, id));
        refreshTable();
    }

    @FXML
    void clearAction(ActionEvent actionEvent) {
        amountText.clear();
        detailsText.clear();
        teacherChoice.getSelectionModel().clearSelection();
    }

    private void refreshTable() {
        tableView.setItems(CRUDService.readAll(ExpenseView.class));
    }
}