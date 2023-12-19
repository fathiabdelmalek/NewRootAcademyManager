package com.fathi.newrootacademymanager.helpers;

import com.fathi.newrootacademymanager.models.Attendance;
import com.fathi.newrootacademymanager.models.AttendanceView;
import com.fathi.newrootacademymanager.services.CRUDService;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
//import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
//import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

public class AttendanceActions implements Callback<TableColumn<AttendanceView, String>, TableCell<AttendanceView, String>> {
    @Override
    public TableCell<AttendanceView, String> call(TableColumn<AttendanceView, String> param) {
        return new TableCell<>() {
            Label number;
            {
                number = new Label(String.valueOf(CRUDService.readById(Attendance.class, getTableView().getSelectionModel().getSelectedItem().getId()).getTimesPresent()));
            }
//            final FontAwesomeIconView decreaseIcon = new FontAwesomeIconView(FontAwesomeIcon.MINUS);
//            final FontAwesomeIconView increaseIcon = new FontAwesomeIconView(FontAwesomeIcon.PLUS);
//            {
//                decreaseIcon.setStyle(
//                        " -fx-cursor: hand ;"
//                                + "-glyph-size:28px;"
//                );
//                increaseIcon.setStyle(
//                        " -fx-cursor: hand ;"
//                                + "-glyph-size:28px;"
//                );
//                decreaseIcon.setOnMouseClicked((MouseEvent event) -> {
//                    Attendance attendance = CRUDService.readById(Attendance.class, getTableView().getSelectionModel().getSelectedItem().getId());
//                    attendance.setTimesPresent(attendance.getTimesPresent() - 1);
//                    CRUDService.update(attendance);
//                    getTableView().setItems(CRUDService.readAll(AttendanceView.class));
//                });
//                increaseIcon.setOnMouseClicked((MouseEvent event) -> {
//                    Attendance attendance = CRUDService.readById(Attendance.class, getTableView().getSelectionModel().getSelectedItem().getId());
//                    attendance.setTimesPresent(attendance.getTimesPresent() + 1);
//                    CRUDService.update(attendance);
//                    getTableView().setItems(CRUDService.readAll(AttendanceView.class));
//                });
//            }

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
//                    HBox actions = new HBox(decreaseIcon, number, increaseIcon);
                    HBox actions = new HBox(number);
                    actions.setStyle("-fx-alignment:center");
                    HBox.setMargin(number, new Insets(2, 2, 0, 3));
                    setGraphic(actions);
                    setText(null);
                }
            }
        };
    }
}
