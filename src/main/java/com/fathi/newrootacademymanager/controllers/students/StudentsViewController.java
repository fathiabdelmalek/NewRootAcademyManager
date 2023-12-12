package com.fathi.newrootacademymanager.controllers.students;

import com.fathi.newrootacademymanager.helpers.enums.Sex;
import com.fathi.newrootacademymanager.models.Grade;
import com.fathi.newrootacademymanager.models.Student;
import com.fathi.newrootacademymanager.models.StudentView;
import com.fathi.newrootacademymanager.services.CRUDService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

public class StudentsViewController {
    @FXML
    private TextField searchText;
    @FXML
    private TableView<StudentView> tableView;
    @FXML
    private TextField firstNameText;
    @FXML
    private TextField lastNameText;
    @FXML
    private TextField phoneNumberText;
    @FXML
    private DatePicker datePicker;
    @FXML
    public RadioButton maleChoice;
    @FXML
    public RadioButton femaleChoice;
    @FXML
    private ComboBox<Grade> gradeChoice;
    @FXML
    private ToggleGroup sex;
    private int id;

    @FXML
    void initialize() {
        Platform.runLater(() -> searchText.requestFocus());

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
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            id = newSelection.getId();
            firstNameText.setText(newSelection.getFirstName());
            lastNameText.setText(newSelection.getLastName());
            phoneNumberText.setText(newSelection.getPhoneNumber());
            datePicker.setValue(newSelection.getBirthDate());
            if ("Male".equalsIgnoreCase(String.valueOf(newSelection.getSex()))) maleChoice.setSelected(true);
            else if ("Female".equalsIgnoreCase(String.valueOf(newSelection.getSex()))) femaleChoice.setSelected(true);
            String selectedGradeString = newSelection.getGrade();
            for (Grade grade : gradeChoice.getItems()) {
                String gradeString = grade.getYear() + " " + grade.getLevel();
                if (gradeString.equalsIgnoreCase(selectedGradeString)) {
                    gradeChoice.setValue(grade);
                    break;
                }
            }
        });
        refreshTable();
    }

    public void searchAction(KeyEvent keyEvent) {
        FilteredList<StudentView> filter = new FilteredList<>(tableView.getItems(), e -> true);
        searchText.textProperty().addListener((Observable, oldValue, newValue) -> {
            filter.setPredicate(predicate -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String key = newValue.toLowerCase();
                return predicate.getFirstName().toLowerCase().contains(key) ||
                        predicate.getLastName().toLowerCase().contains(key) ||
                        predicate.getPhoneNumber().contains(key);
            });
        });
        tableView.setItems(filter);
    }

    public void insertAction(ActionEvent actionEvent) {
        if (firstNameText.getText().isEmpty() ||
                lastNameText.getText().isEmpty() ||
                phoneNumberText.getText().isEmpty() ||
                !sex.getSelectedToggle().isSelected() ||
                datePicker.getValue() == null ||
                gradeChoice.getValue() == null)
            System.out.println("You should insert all required data");
        else {
            CRUDService.create(
                    new Student(
                            firstNameText.getText(),
                            lastNameText.getText(),
                            phoneNumberText.getText(),
                            Sex.valueOf(((RadioButton) sex.getSelectedToggle()).getText()),
                            datePicker.getValue(),
                            gradeChoice.getValue()
                    )
            );
            refreshTable();
        }
    }

    public void updateAction(ActionEvent actionEvent) {
        if (firstNameText.getText().isEmpty() ||
                lastNameText.getText().isEmpty() ||
                phoneNumberText.getText().isEmpty() ||
                !sex.getSelectedToggle().isSelected() ||
                datePicker.getValue() == null ||
                gradeChoice.getValue() == null)
            System.out.println("You should insert all required data");
        else {
            Student student = CRUDService.readById(Student.class, id);
            assert student != null;
            student.setFirstName(firstNameText.getText());
            student.setLastName(lastNameText.getText());
            student.setPhoneNumber(phoneNumberText.getText());
            student.setSex(Sex.valueOf(((RadioButton) sex.getSelectedToggle()).getText()));
            student.setBirthDate(datePicker.getValue());
            student.setGrade(gradeChoice.getValue());
            CRUDService.update(student);
            refreshTable();
        }
    }

    public void deleteAction(ActionEvent actionEvent) {
        CRUDService.delete(CRUDService.readById(Student.class, id));
        refreshTable();
    }

    public void clearAction(ActionEvent actionEvent) {
        firstNameText.clear();
        lastNameText.clear();
        phoneNumberText.clear();
        datePicker.setValue(null);
        maleChoice.setSelected(false);
        femaleChoice.setSelected(false);
        gradeChoice.getSelectionModel().clearSelection();
    }

    private void refreshTable() {
        tableView.setItems(CRUDService.readAll(StudentView.class));
    }
}
