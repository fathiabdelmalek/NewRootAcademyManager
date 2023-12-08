package com.fathi.newrootacademymanager.controllers.lessons;

import com.fathi.newrootacademymanager.models.Grade;
import com.fathi.newrootacademymanager.models.Lesson;
import com.fathi.newrootacademymanager.models.LessonView;
import com.fathi.newrootacademymanager.services.CRUDService;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

import java.time.DayOfWeek;

public class LessonsViewController {
    @FXML
    private Spinner<Integer> startHourSpinner;
    @FXML
    private Spinner<Integer> startMinuteSpinner;
    @FXML
    private Spinner<Integer> endHourSpinner;
    @FXML
    private Spinner<Integer> endMinuteSpinner;
    @FXML
    private TextField searchText;
    @FXML
    private TableView<LessonView> tableView;
    @FXML
    private TableColumn priceColumn;
    @FXML
    private TableColumn studentsColumn;
    @FXML
    private TextField lessonNameText;
    @FXML
    private TextField priceText;
    @FXML
    private ComboBox<Grade> gradeChoice;
    @FXML
    private ComboBox<DayOfWeek> dayChoice;
    @FXML
    private ComboBox<?> teacherChoice;
    private int id;

    @FXML
    void initialize() {
        loadUI();
    }

    @FXML
    void searchAction(KeyEvent event) {
        FilteredList<LessonView> filter = new FilteredList<>(tableView.getItems(), e -> true);
        searchText.textProperty().addListener((Observable, oldValue, newValue) -> {
            filter.setPredicate(predicate -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String key = newValue.toLowerCase();
                return predicate.getLessonName().toLowerCase().contains(key) ||
                        predicate.getGrade().toLowerCase().contains(key) ||
                        predicate.getRoomCode().contains(key) ||
                        String.valueOf(predicate.getPrice()).contains(key);
            });
        });
        tableView.setItems(filter);
    }

    @FXML
    void insertAction(ActionEvent event) {
        refreshTable();
    }

    @FXML
    void updateAction(ActionEvent event) {
        refreshTable();
    }

    @FXML
    void deleteAction(ActionEvent event) {
        CRUDService.delete(CRUDService.readById(Lesson.class, id));
        refreshTable();
    }

    @FXML
    void clearAction(ActionEvent event) {
        lessonNameText.clear();
        priceText.clear();
        gradeChoice.getSelectionModel().clearSelection();
        dayChoice.getSelectionModel().clearSelection();
        startHourSpinner.getValueFactory().setValue(10);
        startMinuteSpinner.getValueFactory().setValue(0);
        endHourSpinner.getValueFactory().setValue(10);
        endMinuteSpinner.getValueFactory().setValue(0);
        teacherChoice.getSelectionModel().clearSelection();
    }

    private void loadUI() {
        priceColumn.setCellFactory(column -> {
            TableCell<LessonView, Float> cell = new TableCell<LessonView, Float>() {
                @Override
                protected void updateItem(Float item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) setText(null);
                    else setText(String.format("%.2f", item));
                }
            };
            return cell;
        });

        gradeChoice.setItems(FXCollections.observableList(CRUDService.readAll(Grade.class)));
        gradeChoice.getItems().addFirst(null);
        gradeChoice.setConverter(new StringConverter<>() {
            @Override
            public String toString(Grade grade) {
                if (grade == null) return null;
                return grade.getLevel() + " " + grade.getYear();
            }

            @Override
            public Grade fromString(String s) {
                return null;
            }
        });

        dayChoice.setItems(FXCollections.observableArrayList(DayOfWeek.values()));
        dayChoice.getItems().addFirst(null);

        SpinnerValueFactory<Integer> hoursRange = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 10);
        SpinnerValueFactory<Integer> minutesRange = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        startHourSpinner.setValueFactory(hoursRange);
        startMinuteSpinner.setValueFactory(minutesRange);
        endHourSpinner.setValueFactory(hoursRange);
        endMinuteSpinner.setValueFactory(minutesRange);

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            id = newSelection.getId();
            lessonNameText.setText(newSelection.getLessonName());
            priceText.setText(String.valueOf(newSelection.getPrice()));
            for (Grade grade : gradeChoice.getItems()) {
                if (grade == null) continue;
                if ((grade.getYear() + " " + grade.getLevel()).equalsIgnoreCase(newSelection.getGrade())) {
                    gradeChoice.setValue(grade);
                    break;
                }
            }
            for (DayOfWeek day : dayChoice.getItems()) {
                if (day == null) continue;
                if (String.valueOf(day).equalsIgnoreCase(String.valueOf(newSelection.getDay()))) {
                    dayChoice.setValue(day);
                    break;
                }
            }
            startHourSpinner.getValueFactory().setValue(newSelection.getStartTime().getHour());
            startMinuteSpinner.getValueFactory().setValue(newSelection.getStartTime().getMinute());
            endHourSpinner.getValueFactory().setValue(newSelection.getEndTime().getHour());
            endMinuteSpinner.getValueFactory().setValue(newSelection.getEndTime().getMinute());
        });
        tableView.setRowFactory(tx -> {
            TableRow<LessonView> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    LessonView selectedLesson = row.getItem();
                    System.out.println("Lesson Name: " + row.getItem().getLessonName());
                }
            });
            return row;
        });
        refreshTable();
    }

    private void refreshTable() {
        tableView.setItems(CRUDService.readAll(LessonView.class));
    }
}
