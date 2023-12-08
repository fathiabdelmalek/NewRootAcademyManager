package com.fathi.newrootacademymanager.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    @FXML
    private AnchorPane topPane;
    @FXML
    private AnchorPane mainPane;
    private double xOffset, yOffset;

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
//        loadView("/com/fathi/newrootacademymanager/views/others/dashboard-view.fxml");
//        loadView("/com/fathi/newrootacademymanager/views/students/students-view.fxml");
        loadView("/com/fathi/newrootacademymanager/views/lessons/lessons-view.fxml");
    }

    public void openDashboardScene(ActionEvent actionEvent) {
        loadView("/com/fathi/newrootacademymanager/views/others/dashboard-view.fxml");

    }

    public void openFinancesScene(ActionEvent actionEvent) {
        loadView("/com/fathi/newrootacademymanager/views/others/finances-view.fxml");
    }

    public void openTableBoardScene(ActionEvent actionEvent) {
        loadView("/com/fathi/newrootacademymanager/views/others/table-board-view.fxml");
    }

    public void openLessonsScene(ActionEvent actionEvent) {
        loadView("/com/fathi/newrootacademymanager/views/lessons/lessons-view.fxml");
    }

    public void openStudentsScene(ActionEvent actionEvent) {
        loadView("/com/fathi/newrootacademymanager/views/students/students-view.fxml");
    }

    public void openTeachersScene(ActionEvent actionEvent) {
        loadView("/com/fathi/newrootacademymanager/views/teachers/teachers-view.fxml");
    }

    public void openGRScene(ActionEvent actionEvent) {
        loadView("/com/fathi/newrootacademymanager/views/others/grades-rooms-view.fxml");
    }

    public void close(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void minimize(ActionEvent actionEvent) {
        Stage stage = (Stage) topPane.getScene().getWindow();
        stage.setIconified(true);
    }

    private void loadView(String viewFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(viewFXML));
            Parent view = loader.load();
            mainPane.getChildren().clear();
            mainPane.getChildren().add(view);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
