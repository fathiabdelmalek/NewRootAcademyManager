package com.fathi.newrootacademymanager.controllers.others;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AboutViewController {
    @FXML
    private AnchorPane topPane;
    private double xOffset, yOffset;

    @FXML
    void initialize() {
        topPane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        topPane.setOnMouseDragged(event -> {
            Stage stage = (Stage) topPane.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    @FXML
    void close(ActionEvent actionEvent) {
        Stage stage = (Stage) topPane.getScene().getWindow();
        stage.close();
    }
}
