package com.fathi.newrootacademymanager.controllers.others;

import com.fathi.newrootacademymanager.helpers.enums.Level;
import com.fathi.newrootacademymanager.models.*;
import com.fathi.newrootacademymanager.services.CRUDService;
import com.fathi.newrootacademymanager.services.CalculationsService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.math.BigDecimal;
import java.util.List;

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
        fillChart();
        getProfitData();
        getLastActivities();
    }

    private void countData() {
        totalStudentsLabel.setText(String.valueOf(CalculationsService.count(Student.class)));
        totalTeachersLabel.setText(String.valueOf(CalculationsService.count(Teacher.class)));
        totalLessonsLabel.setText(String.valueOf(CalculationsService.count(Lesson.class)));
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
