package com.fathi.newrootacademymanager.services;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class DialogsService {

    private DialogsService() {}

    public static void showInformationDialog(String title, String msg) {
        showAlert(title, msg, AlertType.INFORMATION);
    }

    public static void showErrorDialog(String title, String msg) {
        showAlert(title, msg, AlertType.ERROR);
    }

    public static void showWarningDialog(String title, String msg) {
        showAlert(title, msg, AlertType.WARNING);
    }

    public static ButtonType showConfirmationDialog(String title, String msg) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.getButtonTypes().clear();
        alert.getButtonTypes().add(ButtonType.YES);
        alert.getButtonTypes().add(ButtonType.NO);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);

        return alert.showAndWait().orElse(ButtonType.NO);
    }

    private static void showAlert(String title, String msg, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
