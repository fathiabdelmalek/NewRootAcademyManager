package com.fathi.newrootacademymanager;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SplashScreen {

    private Stage splashStage;

    public void show() {
        splashStage = new Stage();
        splashStage.initStyle(StageStyle.UNDECORATED);
        ImageView splashImage = new ImageView(new Image(getClass().getResourceAsStream("images/splash.jpg")));
        splashImage.setPreserveRatio(true);
        splashImage.setFitWidth(400);
        splashStage.setTitle("New Root Academy Manager");
        splashStage.getIcons().add(new Image(getClass().getResourceAsStream("images/logo.png")));
        StackPane splashPane = new StackPane(splashImage);
        Scene splashScene = new Scene(splashPane, 400, 400);
        splashStage.setScene(splashScene);
        splashStage.show();
    }

    public void close() {
        if (splashStage != null) {
            splashStage.close();
        }
    }
}
