package com.fathi.newrootacademymanager.controllers;

import com.fathi.newrootacademymanager.MainApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainViewController {
    @FXML
    private AnchorPane topPane;
    @FXML
    private AnchorPane mainPane;
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
        loadView("/com/fathi/newrootacademymanager/views/others/dashboard-view.fxml");
    }

    @FXML
    void openDashboardScene(ActionEvent actionEvent) {
        loadView("/com/fathi/newrootacademymanager/views/others/dashboard-view.fxml");

    }

    @FXML
    void openFinancesScene(ActionEvent actionEvent) {
        loadView("/com/fathi/newrootacademymanager/views/others/finances-view.fxml");
    }

    @FXML
    void openTableBoardScene(ActionEvent actionEvent) {
        loadView("/com/fathi/newrootacademymanager/views/others/table-board-view.fxml");
    }

    @FXML
    void openLessonsScene(ActionEvent actionEvent) {
        loadView("/com/fathi/newrootacademymanager/views/lessons/lessons-view.fxml");
    }

    @FXML
    void openStudentsScene(ActionEvent actionEvent) {
        loadView("/com/fathi/newrootacademymanager/views/students/students-view.fxml");
    }

    @FXML
    void openTeachersScene(ActionEvent actionEvent) {
        loadView("/com/fathi/newrootacademymanager/views/teachers/teachers-view.fxml");
    }

    @FXML
    void openGRScene(ActionEvent actionEvent) {
        loadView("/com/fathi/newrootacademymanager/views/others/grades-rooms-view.fxml");
    }

    @FXML
    void aboutAction(MouseEvent mouseEvent) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("/com/fathi/newrootacademymanager/views/others/about-view.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(MainViewController.class.getResource("/com/fathi/newrootacademymanager/styles/main.css").toExternalForm());
            scene.getStylesheets().add(MainViewController.class.getResource("/com/fathi/newrootacademymanager/styles/about.css").toExternalForm());
            scene.getStylesheets().add(MainViewController.class.getResource("/com/fathi/newrootacademymanager/styles/top-part.css").toExternalForm());
            scene.getStylesheets().add(MainViewController.class.getResource("/com/fathi/newrootacademymanager/styles/center-part.css").toExternalForm());
            stage.setTitle("About Me");
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void close(ActionEvent actionEvent) {
        System.exit(0);
    }

    @FXML
    void minimize(ActionEvent actionEvent) {
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
