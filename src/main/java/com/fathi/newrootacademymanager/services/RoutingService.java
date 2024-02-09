package com.fathi.newrootacademymanager.services;

import com.fathi.newrootacademymanager.controllers.lessons.LessonDetailsViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class RoutingService {
    private static RoutingService instance;
    private static AnchorPane pane;
    private RoutingService() {}

    public static RoutingService getInstance() {
        if (instance == null) instance = new RoutingService();
        return instance;
    }

    public static void setPane(AnchorPane _pane) {
        pane = _pane;
    }

    public static void navigate(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(RoutingService.class.getResource(path));
            Parent view = loader.load();
            pane.getChildren().clear();
            pane.getChildren().add(view);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void navigateToLessonDetails(int lessonId) {
        try {
            FXMLLoader loader = new FXMLLoader(RoutingService.class.getResource("/com/fathi/newrootacademymanager/views/lessons/lesson-details-view.fxml"));
            Parent view = loader.load();
            LessonDetailsViewController controller = loader.getController();
            controller.initialize(lessonId);
            pane.getChildren().clear();
            pane.getChildren().add(view);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
