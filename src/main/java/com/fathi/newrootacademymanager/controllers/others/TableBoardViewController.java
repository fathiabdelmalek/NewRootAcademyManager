package com.fathi.newrootacademymanager.controllers.others;

import com.fathi.newrootacademymanager.controllers.lessons.LessonDetailsViewController;
import com.fathi.newrootacademymanager.helpers.enums.WeekDay;
import com.fathi.newrootacademymanager.models.LessonView;
import com.fathi.newrootacademymanager.models.Room;
import com.fathi.newrootacademymanager.services.CRUDService;
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

import java.io.IOException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class TableBoardViewController {
    @FXML
    private AnchorPane pane;
    @FXML
    private ComboBox<Room> roomChoice;
    @FXML
    private GridPane grid;

    @FXML
    void initialize() {
        roomChoice.setItems(FXCollections.observableArrayList(CRUDService.readAll(Room.class)));
        roomChoice.setValue(roomChoice.getItems().get(4));
        refreshTable();
    }

    @FXML
    void changeRoomAction(ActionEvent event) {
        refreshTable();
    }

    private void refreshTable() {
        resetTimetable();
        Map<String, Object> params = new HashMap<>();
        params.put("roomCode", roomChoice.getValue().getCode());
        ObservableList<LessonView> lessons = CRUDService.readByCriteria(LessonView.class, params);
        for (LessonView lesson : lessons) {
            String lessonName = lesson.getLessonName();
            String grade = lesson.getGrade();
            int day = getIndexFromDay(lesson.getDay());
            int start = getIndexFromTime(lesson.getStartTime());
            int end = getIndexFromTime(lesson.getEndTime());
            Label label = new Label();
            if (grade == null) label.setText(lessonName);
            else label.setText(lessonName + "\n" + grade);
            label.getStyleClass().add("grid-label");
            String style = label.getStyle();
            label.setStyle(style + "-fx-pref-height: " + (75.0 * (end - start)) + "; -fx-cursor: hand;");
            label.setOnMouseClicked(event -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/fathi/newrootacademymanager/views/lessons/lesson-details-view.fxml"));
                    Parent view = loader.load();
                    LessonDetailsViewController detailsController = loader.getController();
                    detailsController.initialize(lesson.getId());
                    pane.getChildren().clear();
                    pane.getChildren().add(view);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            clearCell(day, start, end - start);
            grid.add(label, day, start, 1, end - start);
        }
    }

    private void resetTimetable() {
        grid.getChildren().clear();
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 10; j++) {
                Label label = new Label("/");
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
        if (time.equals(LocalTime.of(9, 0))) return 0;
        if (time.equals(LocalTime.of(10, 0))) return 1;
        if (time.equals(LocalTime.of(11, 0))) return 2;
        if (time.equals(LocalTime.of(12, 0))) return 3;
        if (time.equals(LocalTime.of(13, 0))) return 4;
        if (time.equals(LocalTime.of(14, 0))) return 5;
        if (time.equals(LocalTime.of(15, 0))) return 6;
        if (time.equals(LocalTime.of(16, 0))) return 7;
        if (time.equals(LocalTime.of(17, 0))) return 8;
        if (time.equals(LocalTime.of(18, 0))) return 9;
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
