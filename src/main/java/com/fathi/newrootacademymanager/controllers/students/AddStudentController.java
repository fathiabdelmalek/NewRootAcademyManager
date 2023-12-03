package com.fathi.newrootacademymanager.controllers.students;

import com.fathi.newrootacademymanager.helpers.enums.Sex;
import com.fathi.newrootacademymanager.models.Grade;
import com.fathi.newrootacademymanager.models.Student;
import com.fathi.newrootacademymanager.services.CRUDService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class AddStudentController {
    @FXML
    private TextField firstNameText;
    @FXML
    private TextField lastNameText;
    @FXML
    private TextField phoneNumberText;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ChoiceBox<Sex> sexChoice;
    @FXML
    private ChoiceBox<Grade> gradeChoice;

    @FXML
    protected void initialize() {
        sexChoice.setItems(FXCollections.observableArrayList(Sex.Male, Sex.Female));
        gradeChoice.setItems(FXCollections.observableList(CRUDService.readAll(Grade.class)));
        gradeChoice.setConverter(new StringConverter<>() {
            @Override
            public String toString(Grade grade) {
                if (grade == null) return null;
                return grade.getLevel() + " " + grade.getYear();
            }

            @Override
            public Grade fromString(String s) {
                return null;
            }
        });
    }

    public void onAddAction(ActionEvent actionEvent) {
        CRUDService.create(
                new Student(
                        firstNameText.getText(),
                        lastNameText.getText(),
                        phoneNumberText.getText(),
                        sexChoice.getValue(),
                        datePicker.getValue(),
                        gradeChoice.getValue()
                )
        );
        hideWindow(actionEvent);
    }

    public void onCancelAction(ActionEvent actionEvent) {
        hideWindow(actionEvent);
    }

    private void hideWindow(ActionEvent actionEvent) {
        ((Button) actionEvent.getSource()).getScene().getWindow().hide();
    }
}
