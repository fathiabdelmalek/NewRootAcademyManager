package com.fathi.newrootacademymanager.controllers.lessons;

import com.fathi.newrootacademymanager.models.AttendanceView;
import com.fathi.newrootacademymanager.models.Lesson;
import com.fathi.newrootacademymanager.services.CRUDService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

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
    private ComboBox gradeChoice;
    @FXML
    private ComboBox studentChoice;
    private int lessonId;
    private int studentId;

    @FXML
    void initialize(int lessonId) {
        this.lessonId = lessonId;
        Lesson lesson = CRUDService.readById(Lesson.class, lessonId);
        teacherNameText.setText((lesson.getTeacher().getFirstName() + " " + lesson.getTeacher().getLastName()));
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            studentNameText.setText(newSelection.getStudentName());
            notesText.setText(newSelection.getNotes());
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
    void decreaseAction(ActionEvent actionEvent) {}

    @FXML
    void increaseAction(ActionEvent actionEvent) {}

    private void refreshTable() {
        tableView.setItems(CRUDService.readAll(AttendanceView.class));
//        String[] params = {"lesson"};
//        String[] values = {String.valueOf(lessonId)};
//        tableView.setItems(CRUDService.readByParams(AttendanceView.class, params, values));
    }
}
