package com.fathi.newrootacademymanager.controllers.others;

import com.fathi.newrootacademymanager.helpers.enums.Level;
import com.fathi.newrootacademymanager.models.Grade;
import com.fathi.newrootacademymanager.models.Lesson;
import com.fathi.newrootacademymanager.models.Student;
import com.fathi.newrootacademymanager.models.Teacher;
import com.fathi.newrootacademymanager.services.CRUDService;
import com.fathi.newrootacademymanager.services.CalculationsService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class DashboardViewController {

    @FXML
    private Label totalStudentsLabel;

    @FXML
    private Label totalTeachersLabel;
    @FXML
    private Label totalLessonsLabel;
    @FXML
    private BarChart<String, Integer> chart;

    @FXML
    void initialize() {
        totalStudentsLabel.setText(String.valueOf(CalculationsService.count(Student.class)));
        totalTeachersLabel.setText(String.valueOf(CalculationsService.count(Teacher.class)));
        totalLessonsLabel.setText(String.valueOf(CalculationsService.count(Lesson.class)));
        fillChart();
    }

    private void fillChart() {
        chart.setLegendVisible(false);
        XYChart.Series<String, Integer> data = new XYChart.Series<>();
        int primaryStudents = CalculationsService.getStudentsWithLevel(Level.Primary);
        int middleStudents = CalculationsService.getStudentsWithLevel(Level.Middle);
        int secondaryStudents = CalculationsService.getStudentsWithLevel(Level.Secondary);
        data.getData().add(new XYChart.Data<>("Primary", primaryStudents));
        data.getData().add(new XYChart.Data<>("Middle", middleStudents));
        data.getData().add(new XYChart.Data<>("Secondary", secondaryStudents));
        chart.getData().add(data);
    }
}
