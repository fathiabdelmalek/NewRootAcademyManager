package com.fathi.newrootacademymanager.controllers.lessons;

import com.fathi.newrootacademymanager.models.*;
import com.fathi.newrootacademymanager.services.CRUDService;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

import java.math.BigDecimal;
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
    public Label salaryLabel;
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
    private Lesson lesson;
    private Student student;
    private int currentClassesNumber;

    @FXML
    void initialize(int lessonId) {
        lesson = CRUDService.readById(Lesson.class, lessonId);
        currentClassesNumber = lesson.getClassesNumber();

        teacherNameText.setText((lesson.getTeacher().getFirstName() + " " + lesson.getTeacher().getLastName()));
        salaryLabel.setText(lesson.getTeacherDues().toString());
        classesNumberText.setText(String.valueOf(lesson.getClassesNumber()));
        teacherPaymentButton.setDisable(BigDecimal.valueOf(Double.parseDouble(salaryLabel.getText())).compareTo(BigDecimal.ZERO) <= 0);

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
            setStudentChoice();
        });

        setStudentChoice();
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
            if (newSelection == null) {
                updateFromSelection(oldSelection);
            } else {
                updateFromSelection(newSelection);
            }
        });
        refreshTable();
    }

    @FXML
    void searchAction(KeyEvent keyEvent) {
        FilteredList<AttendanceView> filter = new FilteredList<>(tableView.getItems(), e -> true);
        searchText.textProperty().addListener((Observable, oldValue, newValue) -> {
            filter.setPredicate(predicate -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String key = newValue.toLowerCase();
                return predicate.getStudentName().toLowerCase().contains(key) ||
                        String.valueOf(predicate.getTimesPresent()).contains(key);
            });
        });
        tableView.setItems(filter);
    }

    @FXML
    void payTeacherAction(ActionEvent actionEvent) {
        Teacher teacher = lesson.getTeacher();
        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(salaryLabel.getText()));
        String details = "pay [" + teacher.getFirstName() + " " + teacher.getLastName() + "] for [" + lesson.getLessonName() + "]";
        CRUDService.create(new Expense(amount, details, teacher));
        lesson.setTeacherDues(new BigDecimal("0.00"));
        CRUDService.update(lesson);
        salaryLabel.setText(lesson.getTeacherDues().toString());
    }

    @FXML
    void decreaseAction(ActionEvent actionEvent) {
        lesson.setClassesNumber(lesson.getClassesNumber() - 1);
        if (lesson.getClassesNumber() == 0 ||
                (lesson.getClassesNumber() % 4 != 0 &&
                        lesson.getClassesNumber() < currentClassesNumber &&
                        currentClassesNumber % 4 == 0))
            lesson.setTeacherDues(lesson.getTeacherDues().subtract(calcTeacherDues()));
        CRUDService.update(lesson);
        classesNumberText.setText(String.valueOf(lesson.getClassesNumber()));
        salaryLabel.setText(lesson.getTeacherDues().toString());
        currentClassesNumber = lesson.getClassesNumber();
        System.out.println(currentClassesNumber);
    }

    @FXML
    void increaseAction(ActionEvent actionEvent) {
        lesson.setClassesNumber(lesson.getClassesNumber() + 1);
        if (lesson.getClassesNumber() % 4 == 0 && lesson.getClassesNumber() > currentClassesNumber)
            lesson.setTeacherDues(lesson.getTeacherDues().add(calcTeacherDues()));
        CRUDService.update(lesson);
        classesNumberText.setText(String.valueOf(lesson.getClassesNumber()));
        salaryLabel.setText(lesson.getTeacherDues().toString());
        currentClassesNumber = lesson.getClassesNumber();
        System.out.println(currentClassesNumber);
    }

    @FXML
    void payAction(ActionEvent actionEvent) {
        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(studentPaymentText.getText()));
        String details = "[" + student.getFirstName() + " " + student.getLastName() + "] pays for [" + lesson.getLessonName() + "]";
        CRUDService.create(new Income(amount, details, student));
        Attendance attendance = CRUDService.readById(Attendance.class, tableView.getSelectionModel().getSelectedItem().getId());
        attendance.setDues(attendance.getDues().subtract(amount));
        CRUDService.update(attendance);
        refreshTable();
    }

    @FXML
    void updateAction(ActionEvent actionEvent) {
        refreshTable();
    }

    @FXML
    void addAction(ActionEvent actionEvent) {
        refreshTable();
    }

    @FXML
    void deleteAction(ActionEvent actionEvent) {
        CRUDService.delete(CRUDService.readById(Attendance.class, tableView.getSelectionModel().getSelectedItem().getId()));
        refreshTable();
    }

    @FXML
    void clearAction(ActionEvent actionEvent) {
        studentNameText.clear();
        studentPaymentText.clear();
        notesText.clear();
        gradeChoice.setValue(lesson.getGrade());
        studentChoice.getSelectionModel().clearSelection();
    }

    private void refreshTable() {
        Map<String, Object> params = new HashMap<>();
        params.put("lesson", lesson);
        tableView.setItems(CRUDService.readByCriteria(AttendanceView.class, params));
    }

    private void setStudentChoice() {
        if (gradeChoice.getValue() == null)
            studentChoice.setItems(FXCollections.observableList(CRUDService.readAll(Student.class)));
        else {
            Map<String, Object> params = new HashMap<>();
            params.put("grade", gradeChoice.getValue());
            studentChoice.setItems(FXCollections.observableList(CRUDService.readByCriteria(Student.class, params)));
        }
        studentChoice.getItems().addFirst(null);
    }

    private void updateFromSelection(AttendanceView oldSelection) {
        student = oldSelection.getStudent();
        studentNameText.setText(oldSelection.getStudentName());
        studentPaymentText.setText(oldSelection.getDues().toString());
        studentPaymentButton.setDisable(oldSelection.getDues().compareTo(BigDecimal.ZERO) <= 0);
        notesText.setText(oldSelection.getNotes());
        gradeChoice.setValue(oldSelection.getStudent().getGrade());
        studentChoice.setValue(oldSelection.getStudent());
    }

    private BigDecimal calcTeacherDues() {
        Map<String, Object> params = new HashMap<>();
        params.put("lesson", lesson);
        BigDecimal amount = new BigDecimal("0.00");
        for (int i = 0; i < CRUDService.readByCriteria(Attendance.class, params).size(); i++) {
            amount = amount.add(lesson.getPrice());
        }
        return amount;
    }
}
