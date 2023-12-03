package com.fathi.newrootacademymanager.helpers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewFactory {
    public static <T> void createAddView(String viewPath, String title) {
        try {
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(new FXMLLoader(ViewFactory.class.getResource(viewPath)).load()));
            stage.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> void createEditView(Class<T> entityClass, String viewPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(ViewFactory.class.getResource(viewPath));
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
//            EditStudentController editStudentController = loader.getController();
//            editStudentController.setStudentData(entity);
            stage.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
