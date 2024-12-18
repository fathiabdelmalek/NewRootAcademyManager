package com.fathi.newrootacademymanager.controllers.others;

import com.fathi.newrootacademymanager.controllers.lessons.LessonDetailsViewController;
import com.fathi.newrootacademymanager.helpers.enums.WeekDay;
import com.fathi.newrootacademymanager.models.Lesson;
import com.fathi.newrootacademymanager.models.Room;
import com.fathi.newrootacademymanager.services.CRUDService;
import com.fathi.newrootacademymanager.services.PrintService;
import com.fathi.newrootacademymanager.services.RoutingService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableBoardViewController {
    @FXML
    private AnchorPane pane;
    @FXML
    private Pane tablePane;
    @FXML
    private ComboBox<Room> roomChoice;
    @FXML
    private GridPane grid;

    @FXML
    void initialize() {
        List<Room> rooms = CRUDService.readAll(Room.class);
        if (rooms != null && !rooms.isEmpty()) {
            roomChoice.setItems(FXCollections.observableArrayList(rooms));
            roomChoice.setValue(roomChoice.getItems().get(0));
        }
        refreshTable();
    }

    @FXML
    void changeRoomAction(ActionEvent event) {
        refreshTable();
    }

    @FXML
    void printAction(ActionEvent event) {
        PrintService.printTableBoard(tablePane);
    }

    private void refreshTable() {
        resetTimetable();
        if (!roomChoice.getSelectionModel().isEmpty()) {
            Map<String, Object> params = new HashMap<>();
            params.put("room", roomChoice.getValue());
            ObservableList<Lesson> lessons = CRUDService.readByCriteria(Lesson.class, params);
            for (Lesson lesson : lessons) {
                String lessonName = lesson.getLessonName();
                int day = getIndexFromDay(lesson.getDayOfWeek());
                int start = getIndexFromTime(lesson.getStartTime());
                int end = getIndexFromTime(lesson.getEndTime());
                Label label = new Label();
                if (lesson.getGrade() != null) label.setText(lessonName + "\n" + lesson.getGrade().toString());
                else label.setText(lessonName + "\n" + lesson.getTeacher());
                label.getStyleClass().add("grid-label");
                String style = label.getStyle();
                label.setStyle(style + "-fx-pref-height: " + (100.0 * (end - start)) + "; -fx-cursor: hand;");
                label.setOnMouseClicked(event -> {
                    RoutingService.navigateToLessonDetails(lesson.getId());
                });
                clearCell(day, start, (end - start));
                grid.add(label, day, start, 1, (end - start));
            }
        }
    }

    private void resetTimetable() {
        grid.getChildren().clear();
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 18; j++) {
                Label label = new Label("");
                label.getStyleClass().add("grid-label");
                grid.add(label, i, j);
            }
        }
    }

    private int getIndexFromDay(WeekDay day) {
        if (day.equals(WeekDay.Saturday)) return 0;
        if (day.equals(WeekDay.Sunday)) return 1;
        if (day.equals(WeekDay.Monday)) return 2;
        if (day.equals(WeekDay.Tuesday)) return 3;
        if (day.equals(WeekDay.Wednesday)) return 4;
        if (day.equals(WeekDay.Thursday)) return 5;
        if (day.equals(WeekDay.Friday)) return 6;
        return -1;
    }

    private int getIndexFromTime(LocalTime time) {
        if (time.equals(LocalTime.of(8, 0))) return 0;
        if (time.equals(LocalTime.of(8, 30))) return 1;
        if (time.equals(LocalTime.of(9, 0))) return 2;
        if (time.equals(LocalTime.of(9, 30))) return 3;
        if (time.equals(LocalTime.of(10, 0))) return 4;
        if (time.equals(LocalTime.of(10, 30))) return 5;
        if (time.equals(LocalTime.of(11, 0))) return 6;
        if (time.equals(LocalTime.of(11, 30))) return 7;
        if (time.equals(LocalTime.of(12, 0))) return 8;
        if (time.equals(LocalTime.of(12, 30))) return 9;
        if (time.equals(LocalTime.of(13, 0))) return 10;
        if (time.equals(LocalTime.of(13, 30))) return 11;
        if (time.equals(LocalTime.of(14, 0))) return 12;
        if (time.equals(LocalTime.of(14, 30))) return 13;
        if (time.equals(LocalTime.of(15, 0))) return 14;
        if (time.equals(LocalTime.of(15, 30))) return 15;
        return -1;
    }

    private void clearCell(int col, int row, int rowspan) {
        for (int i = 0; i < rowspan; i++) {
            for (int j = 0; j < grid.getChildren().size(); j++) {
                if (GridPane.getColumnIndex(grid.getChildren().get(j)) == col &&
                        GridPane.getRowIndex(grid.getChildren().get(j)) == row + i) {
                    grid.getChildren().remove(j);
                    break;
                }
            }
        }
    }
}
