package com.fathi.newrootacademymanager.controllers.lessons;

import com.fathi.newrootacademymanager.models.AttendanceView;
import com.fathi.newrootacademymanager.models.Grade;
import com.fathi.newrootacademymanager.models.Lesson;
import com.fathi.newrootacademymanager.models.Student;
import com.fathi.newrootacademymanager.services.CRUDService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

import java.util.HashMap;
import java.util.Map;

public class LessonDetailsViewController {
    @FXML
    private TextField searchText;
    @FXML
    private TextField teacherNameText;
    @FXML
    private Button teacherPaymentButton;
    @FXML
    private TextField classesNumberText;
    @FXML
    private TableView<AttendanceView> tableView;
    @FXML
    private TableColumn attendancesColumn;
    @FXML
    private TextField studentNameText;
    @FXML
    private TextField studentPaymentText;
    @FXML
    private Button studentPaymentButton;
    @FXML
    private TextField notesText;
    @FXML
    private ComboBox<Grade> gradeChoice;
    @FXML
    private ComboBox<Student> studentChoice;
    Lesson lesson;
    private int studentId;

    @FXML
    void initialize(int lessonId) {
        lesson = CRUDService.readById(Lesson.class, lessonId);

        teacherNameText.setText((lesson.getTeacher().getFirstName() + " " + lesson.getTeacher().getLastName()));
        classesNumberText.setText(String.valueOf(lesson.getClassesNumber()));

        gradeChoice.setItems(FXCollections.observableList(CRUDService.readAll(Grade.class)));
        gradeChoice.getItems().addFirst(null);
        gradeChoice.setValue(lesson.getGrade());
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
        gradeChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Map<String, Object> params = new HashMap<>();
            params.put("grade", gradeChoice.getValue());
            studentChoice.setItems(FXCollections.observableList(CRUDService.readByCriteria(Student.class, params)));
            studentChoice.getItems().addFirst(null);
        });

        Map<String, Object> params = new HashMap<>();
        params.put("grade", gradeChoice.getValue());
        studentChoice.setItems(FXCollections.observableList(CRUDService.readByCriteria(Student.class, params)));
        studentChoice.getItems().addFirst(null);
        studentChoice.setConverter(new StringConverter<>() {
            @Override
            public String toString(Student student) {
                if (student == null) return null;
                return student.getFirstName() + " " + student.getLastName();
            }

            @Override
            public Student fromString(String s) {
                return null;
            }
        });

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            studentNameText.setText(newSelection.getStudentName());
            notesText.setText(newSelection.getNotes());
            studentChoice.setValue(newSelection.getStudent());
        });
        refreshTable();
    }

    @FXML
    void searchAction(KeyEvent keyEvent) {}

    @FXML
    void payAction(ActionEvent actionEvent) {
        refreshTable();
    }

    @FXML
    void addAction(ActionEvent actionEvent) {
        refreshTable();
    }

    @FXML
    void deleteAction(ActionEvent actionEvent) {
        refreshTable();
    }

    @FXML
    void clearAction(ActionEvent actionEvent) {}

    @FXML
    void payTeacherAction(ActionEvent actionEvent) {}

    @FXML
    void decreaseAction(ActionEvent actionEvent) {
        lesson.setClassesNumber(lesson.getClassesNumber() - 1);
        classesNumberText.setText(String.valueOf(lesson.getClassesNumber()));
        CRUDService.update(lesson);
    }

    @FXML
    void increaseAction(ActionEvent actionEvent) {
        lesson.setClassesNumber(lesson.getClassesNumber() + 1);
        classesNumberText.setText(String.valueOf(lesson.getClassesNumber()));
        CRUDService.update(lesson);
    }

    private void refreshTable() {
        Map<String, Object> params = new HashMap<>();
        params.put("lesson", lesson);
        tableView.setItems(CRUDService.readByCriteria(AttendanceView.class, params));
    }
}
