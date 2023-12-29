package com.fathi.newrootacademymanager;

import com.fathi.newrootacademymanager.helpers.DBCManager;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Task<Parent> createMainScene = new Task<>() {
            @Override
            public Parent call() {
                DBCManager.getInstance();
                return null;
            }
        };
        SplashScreen splashScreen = new SplashScreen();
        splashScreen.show();
        createMainScene.setOnSucceeded(e -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("views/main-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                scene.getStylesheets().add(getClass().getResource("styles/style.css").toExternalForm());
                stage.setTitle("New Root Academy Manager");
                stage.getIcons().add(new Image(getClass().getResourceAsStream("images/logo.png")));
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.setScene(scene);
                stage.show();
                splashScreen.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        new Thread(createMainScene).start();
    }

    public static void main(String[] args) {
        launch();
    }
}
