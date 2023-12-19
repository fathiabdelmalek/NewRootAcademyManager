package com.fathi.newrootacademymanager.controllers.others;

import com.fathi.newrootacademymanager.helpers.enums.Level;
import com.fathi.newrootacademymanager.models.*;
import com.fathi.newrootacademymanager.services.CRUDService;
import com.fathi.newrootacademymanager.services.CalculationsService;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.math.BigDecimal;

public class DashboardViewController {

    @FXML
    private Label totalStudentsLabel;

    @FXML
    private Label totalTeachersLabel;
    @FXML
    private Label totalLessonsLabel;
    @FXML
    private BarChart<String, Integer> byLevelsChart;
    @FXML
    private BarChart<String, Integer> byGradesChart;
    @FXML
    private RadioButton levelsChoice;
    @FXML
    private RadioButton gradesChoice;
    @FXML
    private ToggleGroup chart;
    @FXML
    private Label totalIncomeLabel;
    @FXML
    private Label totalExpenseLabel;
    @FXML
    private Label totalProfitLabel;
    @FXML
    private ListView<Activity> activitiesList;

    @FXML
    void initialize() {
        countData();
        fillLevelsChart();
        fillGradesChart();
        getChartChoice();
        getProfitData();
        getLastActivities();
    }

    private void countData() {
        totalStudentsLabel.setText(String.valueOf(CalculationsService.count(Student.class)));
        totalTeachersLabel.setText(String.valueOf(CalculationsService.count(Teacher.class)));
        totalLessonsLabel.setText(String.valueOf(CalculationsService.count(Lesson.class)));
    }

    private void fillLevelsChart() {
        byLevelsChart.setLegendVisible(false);
        XYChart.Series<String, Integer> data = new XYChart.Series<>();
        int primaryStudents = CalculationsService.getStudentsWithLevel(Level.Primary);
        int middleStudents = CalculationsService.getStudentsWithLevel(Level.Middle);
        int secondaryStudents = CalculationsService.getStudentsWithLevel(Level.Secondary);
        data.getData().add(new XYChart.Data<>("Primary", primaryStudents));
        data.getData().add(new XYChart.Data<>("Middle", middleStudents));
        data.getData().add(new XYChart.Data<>("Secondary", secondaryStudents));
        byLevelsChart.getData().add(data);
    }

    private void fillGradesChart() {
        byGradesChart.setLegendVisible(false);
        XYChart.Series<String, Integer> data = new XYChart.Series<>();
        int[] grades = new int[12];
        for (int id = 1; id <= 12; id++) {
            Grade grade = CRUDService.readById(Grade.class, id);
            grades[id-1] = CalculationsService.getStudentsWithGrade(grade);
            data.getData().add(new XYChart.Data<>(grade.toString(), grades[id-1]));
        }
        byGradesChart.getData().add(data);
    }

    private void getChartChoice() {
        chart.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue instanceof RadioButton selected) {
                if (selected.getText().equals("By Level")) {
                    byLevelsChart.setVisible(true);
                    byGradesChart.setVisible(false);
                } else if (selected.getText().equals("By Grade")) {
                    byLevelsChart.setVisible(false);
                    byGradesChart.setVisible(true);
                }
            }
        });
    }

    private void getProfitData() {
        BigDecimal totalIncome = CalculationsService.sum(BigDecimal.class, Income.class, "amount");
        BigDecimal totalExpense = CalculationsService.sum(BigDecimal.class, Expense.class, "amount");
        BigDecimal totalProfit = totalIncome.subtract(totalExpense);
        totalIncomeLabel.setText(totalIncome.toString());
        totalExpenseLabel.setText(totalExpense.toString());
        totalProfitLabel.setText(totalProfit.toString());
    }

    public void getLastActivities() {
        activitiesList.setItems(CRUDService.readAll(Activity.class, 5, "DESC"));
    }
}
