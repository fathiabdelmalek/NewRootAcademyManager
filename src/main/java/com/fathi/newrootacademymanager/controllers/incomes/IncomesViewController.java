package com.fathi.newrootacademymanager.controllers.incomes;

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

public class IncomesViewController {
    @FXML
    private TextField searchText;
    @FXML
    private TableView<IncomeView> tableView;
    @FXML
    private TextField amountText;
    @FXML
    private TextField detailsText;
    @FXML
    private ComboBox<Student> studentChoice;
    private int id;

    @FXML
    void initialize() {
        Platform.runLater(() -> searchText.requestFocus());

        studentChoice.setItems(FXCollections.observableList(CRUDService.readAll(Student.class)));
        studentChoice.getItems().addFirst(null);
        studentChoice.setConverter(new StringConverter<>() {
            @Override
            public String toString(Student student) {
                if (student == null) return null;
                return student.getFirstName() + " " + student.getLastName();
            }

            @Override
            public Student fromString(String string) {
                return null;
            }
        });
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                id = newSelection.getId();
                amountText.setText(String.valueOf(newSelection.getAmount()));
                detailsText.setText(newSelection.getDetails());
                String selectedStudent = newSelection.getStudentName();
                for (Student student : studentChoice.getItems()) {
                    if (student == null) continue;
                    String studentName = student.getFirstName() + " " + student.getLastName();
                    if (studentName.equalsIgnoreCase(selectedStudent)) {
                        studentChoice.setValue(student);
                        break;
                    }
                }
            }
        });
        refreshTable();
    }

    @FXML
    void searchAction(KeyEvent keyEvent) {
        FilteredList<IncomeView> filter = new FilteredList<>(tableView.getItems(), e -> true);
        searchText.textProperty().addListener((Observable, oldValue, newValue) -> {
            filter.setPredicate(predicate -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String key = newValue.toLowerCase();
                if (predicate.getStudentName() != null)
                    return String.valueOf(predicate.getAmount()).contains(key) ||
                            predicate.getTime().toLowerCase().contains(key) ||
                            predicate.getStudentName().toLowerCase().contains(key);
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
                    new Income(
                            new BigDecimal(amountText.getText()),
                            detailsText.getText(),
                            studentChoice.getValue()
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
            Income income = CRUDService.readById(Income.class, id);
            income.setAmount(new BigDecimal(amountText.getText()));
            income.setDetails(detailsText.getText());
            income.setStudent(studentChoice.getValue());
            income.setUpdateTime(LocalDateTime.now());
            CRUDService.update(income);
            refreshTable();
        }
    }

    @FXML
    void deleteAction(ActionEvent actionEvent) {
        CRUDService.delete(CRUDService.readById(Income.class, id));
        refreshTable();
    }

    @FXML
    void clearAction(ActionEvent actionEvent) {
        amountText.clear();
        detailsText.clear();
        studentChoice.getSelectionModel().clearSelection();
    }

    private void refreshTable() {
        tableView.setItems(CRUDService.readAll(IncomeView.class));
    }
}
