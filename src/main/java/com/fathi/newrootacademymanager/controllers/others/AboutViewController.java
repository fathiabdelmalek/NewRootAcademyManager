package com.fathi.newrootacademymanager.controllers.others;

import com.fathi.newrootacademymanager.controllers.MovableController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AboutViewController extends MovableController {
    @FXML
    private AnchorPane topPane;
    private double xOffset, yOffset;

    @FXML
    public void initialize() {
        super.initialize();
    }

    @FXML
    void close(ActionEvent actionEvent) {
        Stage stage = (Stage) topPane.getScene().getWindow();
        stage.close();
    }
}
