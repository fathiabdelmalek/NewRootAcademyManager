package com.fathi.newrootacademymanager.controllers.expenses;

import com.fathi.newrootacademymanager.models.Expense;
import com.fathi.newrootacademymanager.models.Lesson;
import com.fathi.newrootacademymanager.models.Teacher;
import com.fathi.newrootacademymanager.services.CRUDService;
import com.fathi.newrootacademymanager.services.LoggingService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExpensesViewController {
    @FXML
    private TextField searchText;
    @FXML
    private TableView<Expense> tableView;
    @FXML
    private TableColumn<Expense, LocalDateTime> timeColumn;
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

        timeColumn.setCellFactory(column -> {
            TableCell<Expense, LocalDateTime> cell = new TableCell<>() {
                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) setText(null);
                    else setText(item.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                }
            };
            return cell;
        });

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                id = newSelection.getId();
                amountText.setText(String.valueOf(newSelection.getAmount()));
                detailsText.setText(newSelection.getDetails());
                String selectedStudent = newSelection.getTeacher().toString();
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
        FilteredList<Expense> filter = new FilteredList<>(tableView.getItems(), e -> true);
        searchText.textProperty().addListener((Observable, oldValue, newValue) -> {
            filter.setPredicate(predicate -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String key = newValue.toLowerCase();
                if (predicate.getTeacher() != null)
                    return String.valueOf(predicate.getAmount()).contains(key) ||
                            predicate.getCreateTime().toString().toLowerCase().contains(key) ||
                            predicate.getTeacher().toString().toLowerCase().contains(key);
                return String.valueOf(predicate.getAmount()).contains(key) ||
                        predicate.getCreateTime().toString().toLowerCase().contains(key);
            });
        });
        tableView.setItems(filter);
    }

    @FXML
    void insertAction(ActionEvent actionEvent) {
        if (amountText.getText().isEmpty() || detailsText.getText().isEmpty())
            LoggingService.error("You should insert all required fields");
        else {
            Expense expense = new Expense(
                    new BigDecimal(amountText.getText()),
                    detailsText.getText(),
                    teacherChoice.getValue()
            );
            CRUDService.create(expense);
            refreshTable();
            LoggingService.add("Added an expense of [" + expense.getAmount() + " DA]");
        }
    }

    @FXML
    void updateAction(ActionEvent actionEvent) {
        if (amountText.getText().isEmpty() || detailsText.getText().isEmpty())
            LoggingService.error("You should insert all required fields");
        else {
            Expense expense = CRUDService.readById(Expense.class, id);
            BigDecimal oldAmount = expense.getAmount();
            expense.setAmount(new BigDecimal(amountText.getText()));
            expense.setDetails(detailsText.getText());
            expense.setTeacher(teacherChoice.getValue());
            expense.setUpdateTime(LocalDateTime.now());
            CRUDService.update(expense);
            refreshTable();
            LoggingService.update("Changed an expense amount from [" + oldAmount + " DA] to [" + expense.getAmount() + " DA]");
        }
    }

    @FXML
    void deleteAction(ActionEvent actionEvent) {
        Expense expense = CRUDService.readById(Expense.class, id);
        CRUDService.delete(expense);
        refreshTable();
        LoggingService.delete("Remove an expense of [" + expense.getAmount() + " DA]");
    }

    @FXML
    void clearAction(ActionEvent actionEvent) {
        amountText.clear();
        detailsText.clear();
        teacherChoice.getSelectionModel().clearSelection();
    }

    private void refreshTable() {
        tableView.setItems(CRUDService.readAll(Expense.class));
    }
}
