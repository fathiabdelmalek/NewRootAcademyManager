package com.fathi.newrootacademymanager.actions;

import com.fathi.newrootacademymanager.models.Attendance;
import com.fathi.newrootacademymanager.services.CRUDService;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.HashMap;
import java.util.Map;

public class AttendanceActions implements Callback<TableColumn<Attendance, String>, TableCell<Attendance, String>> {
    @Override
    public TableCell<Attendance, String> call(TableColumn<Attendance, String> param) {
        return new TableCell<>() {
            int currentAttendancesNumber;
            final Label number = new Label();
            final FontIcon decreaseIcon = FontIcon.of(FontAwesomeSolid.MINUS);
            final FontIcon increaseIcon = FontIcon.of(FontAwesomeSolid.PLUS);
            {
                decreaseIcon.getStyleClass().add("decrease-action-button");
                increaseIcon.getStyleClass().add("increase-action-button");

                decreaseIcon.setOnMouseClicked((MouseEvent event) -> {
                    Attendance attendance = CRUDService.readById(Attendance.class, getTableView().getSelectionModel().getSelectedItem().getId());
                    if (attendance.getTimesPresent() > 0) {
                        attendance.setTimesPresent(attendance.getTimesPresent() - 1);
                        attendance.setDues(attendance.getDues().subtract(attendance.getLesson().getPrice()));
                        CRUDService.update(attendance);
                        currentAttendancesNumber = attendance.getTimesPresent();
                        Map<String, Object> params = new HashMap<>();
                        params.put("lesson", attendance.getLesson());
                        getTableView().setItems(CRUDService.readByCriteria(Attendance.class, params));
                    }
                });

                increaseIcon.setOnMouseClicked((MouseEvent event) -> {
                    Attendance attendance = CRUDService.readById(Attendance.class, getTableView().getSelectionModel().getSelectedItem().getId());
                    if (attendance.getTimesPresent() < attendance.getLesson().getClassesNumber()) {
                        attendance.setTimesPresent(attendance.getTimesPresent() + 1);
                        attendance.setDues(attendance.getDues().add(attendance.getLesson().getPrice()));
                        CRUDService.update(attendance);
                        currentAttendancesNumber = attendance.getTimesPresent();
                        Map<String, Object> params = new HashMap<>();
                        params.put("lesson", attendance.getLesson());
                        getTableView().setItems(CRUDService.readByCriteria(Attendance.class, params));
                    }
                });
            }

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    number.setText(String.valueOf(CRUDService.readById(Attendance.class, getTableRow().getItem().getId()).getTimesPresent()));
                    HBox actions = new HBox(decreaseIcon, number, increaseIcon);
                    actions.setStyle("-fx-alignment:center");
                    HBox.setMargin(decreaseIcon, new Insets(0, 0, 0, 0));
                    HBox.setMargin(number, new Insets(0, 50, 0, 50));
                    HBox.setMargin(increaseIcon, new Insets(0, 0, 0, 0));
                    setGraphic(actions);
                    setText(null);
                }
            }
        };
    }
}
