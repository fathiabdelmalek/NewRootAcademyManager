package com.fathi.newrootacademymanager.controllers;

import com.fathi.newrootacademymanager.MainApplication;
import com.fathi.newrootacademymanager.services.DialogsService;
import com.fathi.newrootacademymanager.services.RoutingService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainViewController extends MovableController {
    @FXML
    private AnchorPane mainPane;

    @FXML
    public void initialize() {
        super.initialize();
        RoutingService.navigate("/com/fathi/newrootacademymanager/views/others/dashboard-view.fxml");
    }

    @FXML
    void openDashboardScene(ActionEvent actionEvent) {
        RoutingService.navigate("/com/fathi/newrootacademymanager/views/others/dashboard-view.fxml");

    }

    @FXML
    void openFinancesScene(ActionEvent actionEvent) {
        RoutingService.navigate("/com/fathi/newrootacademymanager/views/others/finances-view.fxml");
    }

    @FXML
    void openTableBoardScene(ActionEvent actionEvent) {
        RoutingService.navigate("/com/fathi/newrootacademymanager/views/others/table-board-view.fxml");
    }

    @FXML
    void openLessonsScene(ActionEvent actionEvent) {
        RoutingService.navigate("/com/fathi/newrootacademymanager/views/lessons/lessons-view.fxml");
    }

    @FXML
    void openStudentsScene(ActionEvent actionEvent) {
        RoutingService.navigate("/com/fathi/newrootacademymanager/views/students/students-view.fxml");
    }

    @FXML
    void openTeachersScene(ActionEvent actionEvent) {
        RoutingService.navigate("/com/fathi/newrootacademymanager/views/teachers/teachers-view.fxml");
    }

    @FXML
    void openGRScene(ActionEvent actionEvent) {
        RoutingService.navigate("/com/fathi/newrootacademymanager/views/others/grades-rooms-view.fxml");
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
            RoutingService.setPane(mainPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
