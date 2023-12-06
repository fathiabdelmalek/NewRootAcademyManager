package com.fathi.newrootacademymanager.controllers.others;

import com.fathi.newrootacademymanager.models.Grade;
import com.fathi.newrootacademymanager.models.Room;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class GradesRoomsViewController {
    @FXML
    public TextField searchGradeText;
    @FXML
    public TextField searchRoomText;
    @FXML
    public ListView<Grade> gradesList;
    @FXML
    public TextField levelText;
    @FXML
    public TextField yearText;
    @FXML
    public ListView<Room> roomsList;
    @FXML
    public TextField codeText;

    public void initialize() {}

    public void searchGradeAction() {}

    public void searchRoomAction() {}

    public void insertGrade() {}

    public void updateGrade() {}

    public void deleteGrade() {}

    public void clearGrade() {}

    public void insertRoom() {}

    public void updateRoom() {}

    public void deleteRoom() {}

    public void clearRoom() {}
}
