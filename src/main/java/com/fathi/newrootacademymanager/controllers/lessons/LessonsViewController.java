package com.fathi.newrootacademymanager.controllers.lessons;

import com.fathi.newrootacademymanager.helpers.enums.WeekDay;
import com.fathi.newrootacademymanager.models.*;
import com.fathi.newrootacademymanager.services.CRUDService;
import com.fathi.newrootacademymanager.services.CalculationsService;
import com.fathi.newrootacademymanager.services.LoggingService;
import com.fathi.newrootacademymanager.services.RoutingService;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class LessonsViewController {
    @FXML
    public AnchorPane pane;
    @FXML
    private Spinner<Integer> startHourSpinner;
    @FXML
    private Spinner<Integer> startMinuteSpinner;
    @FXML
    private TextField searchText;
    @FXML
    private TableView<Lesson> tableView;
    @FXML
    private TableColumn<Lesson, Integer> studentsColumn;
    @FXML
    private TextField lessonNameText;
    @FXML
    private TextField priceText;
    @FXML
    private ComboBox<Grade> gradeChoice;
    @FXML
    private ComboBox<WeekDay> dayChoice;
    @FXML
    private ComboBox<Room> roomChoice;
    @FXML
    private ComboBox<Teacher> teacherChoice;
    @FXML
    private Spinner<Integer> percentageSpinner;
    private int id;

    @FXML
    void initialize() {
        Platform.runLater(() -> searchText.requestFocus());

        gradeChoice.setItems(FXCollections.observableList(CRUDService.readAll(Grade.class)));
        gradeChoice.getItems().add(0, null);

        dayChoice.setItems(FXCollections.observableArrayList(WeekDay.values()));
        dayChoice.getItems().add(0, null);

        SpinnerValueFactory<Integer> startHoursRange = new SpinnerValueFactory.IntegerSpinnerValueFactory(8, 16, 8);
        SpinnerValueFactory<Integer> startMinutesRange = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 30, 0, 30);
        startHourSpinner.setValueFactory(startHoursRange);
        startMinuteSpinner.setValueFactory(startMinutesRange);

        roomChoice.setItems(FXCollections.observableArrayList(CRUDService.readAll(Room.class)));
        roomChoice.getItems().add(0, null);

        teacherChoice.setItems(FXCollections.observableList(CRUDService.readAll(Teacher.class)));
        teacherChoice.getItems().add(0, null);

        SpinnerValueFactory<Integer> percentageRange = new SpinnerValueFactory.IntegerSpinnerValueFactory(40, 70, 50, 5);
        percentageSpinner.setValueFactory(percentageRange);

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                id = newSelection.getId();
                lessonNameText.setText(newSelection.getLessonName());
                priceText.setText(String.valueOf(newSelection.getPrice()));
                for (Grade grade : gradeChoice.getItems()) {
                    if (newSelection.getGrade() == null) {
                        gradeChoice.getSelectionModel().clearSelection();
                        break;
                    }
                    if (grade == null) continue;
                    if ((grade.getYearOfGrade() + " " + grade.getLevel()).equalsIgnoreCase(newSelection.getGrade().toString())) {
                        gradeChoice.setValue(grade);
                        break;
                    }
                }
                for (WeekDay day : dayChoice.getItems()) {
                    if (day == null) continue;
                    if (String.valueOf(day).equalsIgnoreCase(String.valueOf(newSelection.getDayOfWeek()))) {
                        dayChoice.setValue(day);
                        break;
                    }
                }
                startHourSpinner.getValueFactory().setValue(newSelection.getStartTime().getHour());
                startMinuteSpinner.getValueFactory().setValue(newSelection.getStartTime().getMinute());
                for (Room room : roomChoice.getItems()) {
                    if (room == null) continue;
                    if (room.getCode().equalsIgnoreCase(newSelection.getRoom().toString())) {
                        roomChoice.setValue(room);
                        break;
                    }
                }
                for (Teacher teacher : teacherChoice.getItems()) {
                    if (teacher == null) continue;
                    if ((teacher.getFirstName() + " " + teacher.getLastName()).equalsIgnoreCase(newSelection.getTeacher().toString())) {
                        teacherChoice.setValue(teacher);
                        break;
                    }
                }
                percentageSpinner.getValueFactory().setValue(newSelection.getPercentage());
            }
        });
        tableView.setRowFactory(tx -> {
            TableRow<Lesson> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    RoutingService.navigateToLessonDetails(row.getItem().getId());
                }
            });
            return row;
        });
        refreshTable();
    }

    @FXML
    void searchAction(KeyEvent event) {
        FilteredList<Lesson> filter = new FilteredList<>(tableView.getItems(), e -> true);
        searchText.textProperty().addListener((Observable, oldValue, newValue) -> {
            filter.setPredicate(predicate -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String key = newValue.toLowerCase();
                return predicate.getLessonName().toLowerCase().contains(key) ||
                        (predicate.getGrade() != null && predicate.getGrade().toString().toLowerCase().contains(key)) ||
                        predicate.getDayOfWeek().toString().toLowerCase().contains(key) ||
                        String.valueOf(predicate.getPrice()).contains(key) ||
                        predicate.getRoom().toString().toLowerCase().contains(key) ||
                        predicate.getTeacher().toString().toLowerCase().contains(key);
            });
        });
        tableView.setItems(filter);
    }

    @FXML
    void insertAction(ActionEvent event) {
        if (lessonNameText.getText().isEmpty() ||
                priceText.getText().isEmpty() ||
                dayChoice.getValue() == null ||
                roomChoice.getValue() == null ||
                teacherChoice.getValue() == null)
            LoggingService.error("You should insert all required fields");
        else {
            Lesson lesson = new Lesson(
                    lessonNameText.getText(),
                    dayChoice.getValue(),
                    LocalTime.of(startHourSpinner.getValue(), startMinuteSpinner.getValue()),
                    LocalTime.of(startHourSpinner.getValue() + 1, startMinuteSpinner.getValue() + 30),
                    BigDecimal.valueOf(Double.parseDouble(priceText.getText())),
                    percentageSpinner.getValue(),
                    teacherChoice.getValue(),
                    roomChoice.getValue(),
                    gradeChoice.getValue()
            );
            CRUDService.create(lesson);
            refreshTable();
            LoggingService.add("Lesson " + lesson + " have been added");
        }
    }

    @FXML
    void updateAction(ActionEvent event) {
        if (lessonNameText.getText().isEmpty() ||
                priceText.getText().isEmpty() ||
                dayChoice.getValue() == null ||
                roomChoice.getValue() == null ||
                teacherChoice.getValue() == null) {
            LoggingService.error("You should insert all required fields");
        } else {
            Lesson lesson = CRUDService.readById(Lesson.class, id);
            lesson.setLessonName(lessonNameText.getText());
            lesson.setDayOfWeek(dayChoice.getValue());
            lesson.setStartTime(LocalTime.of(startHourSpinner.getValue(), startMinuteSpinner.getValue()));
            lesson.setEndTime(LocalTime.of(startHourSpinner.getValue() + 1, startMinuteSpinner.getValue() + 30));
            lesson.setPrice(BigDecimal.valueOf(Double.parseDouble(priceText.getText())));
            lesson.setGrade(gradeChoice.getValue());
            lesson.setRoom(roomChoice.getValue());
            lesson.setTeacher(teacherChoice.getValue());
            lesson.setPercentage(percentageSpinner.getValue());
            CRUDService.update(lesson);
            refreshTable();
            LoggingService.update("Lesson " + lesson + " have been updated");
        }
    }

    @FXML
    void deleteAction(ActionEvent event) {
        Lesson lesson = CRUDService.readById(Lesson.class, id);
        CRUDService.delete(lesson);
        refreshTable();
        LoggingService.delete("Lesson " + lesson + " have been deleted");
    }

    @FXML
    void clearAction(ActionEvent event) {
        lessonNameText.clear();
        priceText.clear();
        gradeChoice.getSelectionModel().clearSelection();
        dayChoice.getSelectionModel().clearSelection();
        startHourSpinner.getValueFactory().setValue(8);
        startMinuteSpinner.getValueFactory().setValue(0);
        roomChoice.getSelectionModel().clearSelection();
        teacherChoice.getSelectionModel().clearSelection();
        percentageSpinner.getValueFactory().setValue(50);
    }

    private void refreshTable() {
        tableView.setItems(CRUDService.readAll(Lesson.class));
        studentsColumn.setCellValueFactory(data -> {
            Map<String, Object> params = new HashMap<>();
            params.put("lesson", data.getValue());
            int numberOfStudents = CalculationsService.count(Attendance.class, params);
            return new SimpleObjectProperty<>(numberOfStudents);
        });
    }
}
