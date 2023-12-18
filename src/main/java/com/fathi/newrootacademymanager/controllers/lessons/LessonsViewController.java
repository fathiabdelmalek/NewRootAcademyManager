package com.fathi.newrootacademymanager.controllers.lessons;

import com.fathi.newrootacademymanager.helpers.enums.WeekDay;
import com.fathi.newrootacademymanager.models.*;
import com.fathi.newrootacademymanager.services.CRUDService;
import com.fathi.newrootacademymanager.services.LoggingService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalTime;

public class LessonsViewController {
    @FXML
    public AnchorPane pane;
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
    private ComboBox<WeekDay> dayChoice;
    @FXML
    private ComboBox<Room> roomChoice;
    @FXML
    private ComboBox<Teacher> teacherChoice;
    private int id;

    @FXML
    void initialize() {
        Platform.runLater(() -> searchText.requestFocus());

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

        dayChoice.setItems(FXCollections.observableArrayList(WeekDay.values()));
        dayChoice.getItems().addFirst(null);

        SpinnerValueFactory<Integer> startHoursRange = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 10);
        SpinnerValueFactory<Integer> startMinutesRange = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        SpinnerValueFactory<Integer> endHoursRange = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 11);
        SpinnerValueFactory<Integer> endMinutesRange = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        startHourSpinner.setValueFactory(startHoursRange);
        startMinuteSpinner.setValueFactory(startMinutesRange);
        endHourSpinner.setValueFactory(endHoursRange);
        endMinuteSpinner.setValueFactory(endMinutesRange);

        roomChoice.setItems(FXCollections.observableArrayList(CRUDService.readAll(Room.class)));
        roomChoice.getItems().addFirst(null);
        roomChoice.setConverter(new StringConverter<>() {
            @Override
            public String toString(Room room) {
                return room.getCode();
            }

            @Override
            public Room fromString(String s) {
                return null;
            }
        });

        teacherChoice.setItems(FXCollections.observableList(CRUDService.readAll(Teacher.class)));
        teacherChoice.getItems().addFirst(null);
        teacherChoice.setConverter(new StringConverter<>() {
            @Override
            public String toString(Teacher teacher) {
                if (teacher == null) return null;
                return teacher.getFirstName() + " " + teacher.getLastName();
            }

            @Override
            public Teacher fromString(String s) {
                return null;
            }
        });

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
                    if ((grade.getYear() + " " + grade.getLevel()).equalsIgnoreCase(newSelection.getGrade())) {
                        gradeChoice.setValue(grade);
                        break;
                    }
                }
                for (WeekDay day : dayChoice.getItems()) {
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
                for (Room room : roomChoice.getItems()) {
                    if (room == null) continue;
                    if (room.getCode().equalsIgnoreCase(newSelection.getRoomCode())) {
                        roomChoice.setValue(room);
                        break;
                    }
                }
                for (Teacher teacher : teacherChoice.getItems()) {
                    if (teacher == null) continue;
                    if ((teacher.getFirstName() + " " + teacher.getLastName()).equalsIgnoreCase(newSelection.getTeacherName())) {
                        teacherChoice.setValue(teacher);
                        break;
                    }
                }
            }
        });
        tableView.setRowFactory(tx -> {
            TableRow<LessonView> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    int lessonId = row.getItem().getId();
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/fathi/newrootacademymanager/views/lessons/lesson-details-view.fxml"));
                        Parent view = loader.load();
                        LessonDetailsViewController detailsController = loader.getController();
                        detailsController.initialize(lessonId);
                        pane.getChildren().clear();
                        pane.getChildren().add(view);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            return row;
        });
        refreshTable();
    }

    @FXML
    void searchAction(KeyEvent event) {
        FilteredList<LessonView> filter = new FilteredList<>(tableView.getItems(), e -> true);
        searchText.textProperty().addListener((Observable, oldValue, newValue) -> {
            filter.setPredicate(predicate -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String key = newValue.toLowerCase();
                return predicate.getLessonName().toLowerCase().contains(key) ||
                        (predicate.getGrade() != null && predicate.getGrade().toLowerCase().contains(key)) ||
                        predicate.getDay().toString().toLowerCase().contains(key) ||
                        String.valueOf(predicate.getPrice()).contains(key) ||
                        predicate.getRoomCode().toLowerCase().contains(key) ||
                        predicate.getTeacherName().toLowerCase().contains(key);
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
                    LocalTime.of(endHourSpinner.getValue(), endMinuteSpinner.getValue()),
                    BigDecimal.valueOf(Double.parseDouble(priceText.getText())),
                    teacherChoice.getValue(),
                    roomChoice.getValue(),
                    gradeChoice.getValue()
            );
            CRUDService.create(lesson);
            refreshTable();
            LoggingService.add(lesson + " have been added");
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
            lesson.setDay(dayChoice.getValue());
            lesson.setStartTime(LocalTime.of(startHourSpinner.getValue(), startMinuteSpinner.getValue()));
            lesson.setEndTime(LocalTime.of(endHourSpinner.getValue(), endMinuteSpinner.getValue()));
            lesson.setPrice(BigDecimal.valueOf(Double.parseDouble(priceText.getText())));
            lesson.setTeacher(teacherChoice.getValue());
            lesson.setRoom(roomChoice.getValue());
            lesson.setGrade(gradeChoice.getValue());
            CRUDService.update(lesson);
            refreshTable();
            LoggingService.update(lesson + " have been updated");
        }
    }

    @FXML
    void deleteAction(ActionEvent event) {
        Lesson lesson = CRUDService.readById(Lesson.class, id);
        CRUDService.delete(lesson);
        refreshTable();
        LoggingService.delete(lesson + " have been deleted");
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

    private void refreshTable() {
        tableView.setItems(CRUDService.readAll(LessonView.class));
    }
}
