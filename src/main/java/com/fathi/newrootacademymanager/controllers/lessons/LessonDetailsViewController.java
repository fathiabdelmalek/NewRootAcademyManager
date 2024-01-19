package com.fathi.newrootacademymanager.controllers.lessons;

import com.fathi.newrootacademymanager.helpers.AttendanceActions;
import com.fathi.newrootacademymanager.models.Attendance;
import com.fathi.newrootacademymanager.models.Lesson;
import com.fathi.newrootacademymanager.models.Student;
import com.fathi.newrootacademymanager.models.Teacher;
import com.fathi.newrootacademymanager.models.Grade;
import com.fathi.newrootacademymanager.models.Expense;
import com.fathi.newrootacademymanager.models.Income;
import com.fathi.newrootacademymanager.services.CRUDService;
import com.fathi.newrootacademymanager.services.LoggingService;
import com.fathi.newrootacademymanager.services.PrintService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.thymeleaf.context.Context;

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
    private TextField salaryText;
    @FXML
    private TextField classesNumberText;
    @FXML
    private TableView<Attendance> tableView;
    @FXML
    private TableColumn<Attendance, String> attendancesColumn;
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
    private Attendance latestSelection;
    private Lesson lesson;
    private Student student;

    @FXML
    public void initialize(int lessonId) {
        Platform.runLater(() -> searchText.requestFocus());

        lesson = CRUDService.readById(Lesson.class, lessonId);

        teacherNameText.setText((lesson.getTeacher().getFirstName() + " " + lesson.getTeacher().getLastName()));
        salaryText.setText(lesson.getTeacherDues().toString());
        classesNumberText.setText(String.valueOf(lesson.getClassesNumber()));
        teacherPaymentButton.setDisable(BigDecimal.valueOf(Double.parseDouble(salaryText.getText())).compareTo(BigDecimal.ZERO) <= 0);

        gradeChoice.setItems(FXCollections.observableList(CRUDService.readAll(Grade.class)));
        gradeChoice.getItems().addFirst(null);
        gradeChoice.setValue(lesson.getGrade());
        gradeChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setStudentChoice();
        });

        setStudentChoice();

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection == null) {
                updateFromSelection(oldSelection);
                latestSelection = oldSelection;
            } else {
                updateFromSelection(newSelection);
                latestSelection = newSelection;
            }
        });
        refreshTable();
        attendancesColumn.setCellFactory(new AttendanceActions());
    }

    @FXML
    void searchAction(KeyEvent keyEvent) {
        FilteredList<Attendance> filter = new FilteredList<>(tableView.getItems(), e -> true);
        searchText.textProperty().addListener((Observable, oldValue, newValue) -> filter.setPredicate(predicate -> {
            if (newValue == null || newValue.isEmpty()) return true;
            String key = newValue.toLowerCase();
            return predicate.getStudent().toString().toLowerCase().contains(key) ||
                    String.valueOf(predicate.getTimesPresent()).contains(key);
        }));
        tableView.setItems(filter);
    }

    @FXML
    void payTeacherAction(ActionEvent actionEvent) {
        Teacher teacher = lesson.getTeacher();
        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(salaryText.getText()));
        BigDecimal rest = lesson.getTeacherDues().subtract(amount);
        String details = "payed " + teacher + " [" + amount + " DA] for " + lesson + " | rest is [" + rest + " DA]";
        CRUDService.create(new Expense(amount, details, teacher));
        lesson.setTeacherDues(rest);
        CRUDService.update(lesson);
        salaryText.setText(lesson.getTeacherDues().toString());
        LoggingService.pay(details);
    }

    @FXML
    void decreaseAction(ActionEvent actionEvent) {
        if (lesson.getClassesNumber() > 0) {
            lesson.setClassesNumber(lesson.getClassesNumber() - 1);
            CRUDService.update(lesson);
            classesNumberText.setText(String.valueOf(lesson.getClassesNumber()));
            LoggingService.update("Lesson " + lesson + " number of lessons decreased by one and become " + lesson.getClassesNumber());
        }
    }

    @FXML
    void increaseAction(ActionEvent actionEvent) {
        lesson.setClassesNumber(lesson.getClassesNumber() + 1);
        CRUDService.update(lesson);
        classesNumberText.setText(String.valueOf(lesson.getClassesNumber()));
        LoggingService.update("Lesson " + lesson + " number of lessons increased by one and become " + lesson.getClassesNumber());
    }

    @FXML
    void payAction(ActionEvent actionEvent) {
        Attendance attendance = CRUDService.readById(Attendance.class, latestSelection.getId());
        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(studentPaymentText.getText()));
        BigDecimal rest = attendance.getDues().subtract(amount);
        String details = "Student " + student + " pays [" + amount + " DA] for Lesson " + lesson + " | rest is [" + rest + " DA]";
        CRUDService.create(new Income(amount, details, student));
        attendance.setDues(attendance.getDues().subtract(amount));
        CRUDService.update(attendance);
        refreshTable();
        lesson.setTeacherDues(lesson.getTeacherDues().add(BigDecimal.valueOf(amount.longValue() * lesson.getPercentage() / 100)));
        CRUDService.update(lesson);
        salaryText.setText(lesson.getTeacherDues().toString());
        updateFromSelection(latestSelection);
        LoggingService.receive(details);
    }

    @FXML
    void updateAction(ActionEvent actionEvent) {
        if (studentNameText.getText().isEmpty())
            System.out.println("You should insert all required data");
        else {
            Attendance attendance = CRUDService.readById(Attendance.class, tableView.getSelectionModel().getSelectedItem().getId());
            attendance.setNotes(notesText.getText());
            CRUDService.update(attendance);
            refreshTable();
            LoggingService.update("Lesson " + lesson + " have been updated");
        }
    }

    @FXML
    void addAction(ActionEvent actionEvent) {
        if (studentChoice.getValue() == null)
            System.out.println("You should insert all required data");
        else {
            Attendance attendance = new Attendance(lesson, studentChoice.getValue(), notesText.getText());
            CRUDService.create(attendance);
            refreshTable();
            LoggingService.enroll("Student " + attendance.getStudent() + " enrolled the lesson " + attendance.getLesson());
        }
    }

    @FXML
    void deleteAction(ActionEvent actionEvent) {
        Attendance attendance = CRUDService.readById(Attendance.class, tableView.getSelectionModel().getSelectedItem().getId());
        CRUDService.delete(attendance);
        refreshTable();
        LoggingService.leave("Student " + attendance.getStudent() + " leaved the lesson " + attendance.getLesson());
    }

    @FXML
    void clearAction(ActionEvent actionEvent) {
        studentNameText.clear();
        studentPaymentText.clear();
        notesText.clear();
        gradeChoice.setValue(lesson.getGrade());
        studentChoice.getSelectionModel().clearSelection();
    }

    @FXML
    void printAction(ActionEvent actionEvent) {
        Context context = new Context();
        context.setVariable("lesson", lesson);
        context.setVariable("attendances", tableView.getItems());
        PrintService.printContent(context, getClass().getResourceAsStream("/com/fathi/newrootacademymanager/templates/lesson-template.html"));
    }

    private void refreshTable() {
        Map<String, Object> params = new HashMap<>();
        params.put("lesson", lesson);
        tableView.setItems(CRUDService.readByCriteria(Attendance.class, params));
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

    private void updateFromSelection(Attendance selection) {
        student = CRUDService.readById(Student.class, selection.getStudent().getId());
        studentNameText.setText(selection.getStudent().toString());
        studentPaymentText.setText(selection.getDues().toString());
        studentPaymentButton.setDisable(selection.getDues().compareTo(BigDecimal.ZERO) <= 0);
        notesText.setText(selection.getNotes());
        gradeChoice.setValue(student.getGrade());
        studentChoice.setValue(student);
    }
}
