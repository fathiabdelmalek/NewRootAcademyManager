package com.fathi.newrootacademymanager.helpers;

import com.fathi.newrootacademymanager.models.Attendance;
import com.fathi.newrootacademymanager.models.AttendanceView;
import com.fathi.newrootacademymanager.services.CRUDService;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.HashMap;
import java.util.Map;

public class AttendanceActions implements Callback<TableColumn<AttendanceView, String>, TableCell<AttendanceView, String>> {
    @Override
    public TableCell<AttendanceView, String> call(TableColumn<AttendanceView, String> param) {
        return new TableCell<>() {
            int currentAttendancesNumber;
            final Label number = new Label();
            final FontIcon decreaseIcon = FontIcon.of(FontAwesomeSolid.MINUS);
            final FontIcon increaseIcon = FontIcon.of(FontAwesomeSolid.PLUS);
            {


                decreaseIcon.setIconColor(Color.valueOf("#E61744"));
                decreaseIcon.setCursor(Cursor.HAND);
                decreaseIcon.setIconSize(24);

                increaseIcon.setIconSize(24);
                increaseIcon.setIconColor(Color.valueOf("#0076E6"));
                increaseIcon.setCursor(Cursor.HAND);

                decreaseIcon.setOnMouseClicked((MouseEvent event) -> {
                    Attendance attendance = CRUDService.readById(Attendance.class, getTableView().getSelectionModel().getSelectedItem().getId());
                    attendance.setTimesPresent(attendance.getTimesPresent() - 1);
                    if (attendance.getTimesPresent() == 0 ||
                            (attendance.getTimesPresent() % 4 != 0 &&
                                    attendance.getTimesPresent() < currentAttendancesNumber &&
                                    currentAttendancesNumber % 4 == 0))
                        attendance.setDues(attendance.getDues().subtract(attendance.getLesson().getPrice()));
                    CRUDService.update(attendance);
                    currentAttendancesNumber = attendance.getTimesPresent();
                    Map<String, Object> params = new HashMap<>();
                    params.put("lesson", attendance.getLesson().getId());
                    getTableView().setItems(CRUDService.readByCriteria(AttendanceView.class, params));
                });

                increaseIcon.setOnMouseClicked((MouseEvent event) -> {
                    Attendance attendance = CRUDService.readById(Attendance.class, getTableView().getSelectionModel().getSelectedItem().getId());
                    attendance.setTimesPresent(attendance.getTimesPresent() + 1);
                    if (attendance.getTimesPresent() % 4 == 0 && attendance.getTimesPresent() > currentAttendancesNumber)
                        attendance.setDues(attendance.getDues().add(attendance.getLesson().getPrice()));
                    CRUDService.update(attendance);
                    currentAttendancesNumber = attendance.getTimesPresent();
                    Map<String, Object> params = new HashMap<>();
                    params.put("lesson", attendance.getLesson().getId());
                    getTableView().setItems(CRUDService.readByCriteria(AttendanceView.class, params));
                });
            }

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    number.setText(String.valueOf(CRUDService.readById(AttendanceView.class, getTableRow().getItem().getId()).getTimesPresent()));
                    HBox actions = new HBox(decreaseIcon, number, increaseIcon);
                    actions.setStyle("-fx-alignment:center");
                    HBox.setMargin(decreaseIcon, new Insets(2, 2, 0, 3));
                    HBox.setMargin(number, new Insets(2, 2, 0, 3));
                    HBox.setMargin(increaseIcon, new Insets(2, 2, 0, 3));
                    setGraphic(actions);
                    setText(null);
                }
            }
        };
    }
}
