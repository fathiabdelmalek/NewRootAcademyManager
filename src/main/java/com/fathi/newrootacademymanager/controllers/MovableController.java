package com.fathi.newrootacademymanager.controllers;

import com.fathi.newrootacademymanager.services.DialogsService;
import com.fathi.newrootacademymanager.services.RoutingService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MovableController {
    @FXML
    private AnchorPane topPane;
    @FXML
    private AnchorPane mainPane;
    private double xOffset, yOffset;
    @FXML
    public void initialize() {
        topPane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        topPane.setOnMouseDragged(event -> {
            Stage stage = (Stage) topPane.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
        RoutingService.setPane(mainPane);
    }

    @FXML
    void close(ActionEvent actionEvent) {
        if (DialogsService.showConfirmationDialog("Confirm Exit", "Do you really want to exit?") == ButtonType.YES)
            System.exit(0);
    }

    @FXML
    void minimize(ActionEvent actionEvent) {
        Stage stage = (Stage) topPane.getScene().getWindow();
        stage.setIconified(true);
    }
}
