package com.fathi.newrootacademymanager.controllers.others;

import com.fathi.newrootacademymanager.models.Expense;
import com.fathi.newrootacademymanager.models.Income;
import com.fathi.newrootacademymanager.services.CalculationsService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FinancesViewController {
    @FXML
    private AnchorPane pane;
    @FXML
    private Label filteredExpenseLabel;
    @FXML
    private Label filteredIncomeLabel;
    @FXML
    private Label filteredProfitLabel;
    @FXML
    private Label totalExpenseLabel;
    @FXML
    private Label totalIncomeLabel;
    @FXML
    private Label totalProfitLabel;
    @FXML
    private LineChart<String, BigDecimal> chart;
    @FXML
    private DatePicker datePicker;

    @FXML
    void initialize() {
        LocalDate filterTime = LocalDate.now().minusMonths(1);
        datePicker.setValue(filterTime);

        getTotalProfitData();
        getFilteredProfitData(filterTime);
        fillChart();
    }

    @FXML
    void filterAction(ActionEvent event) {
        LocalDate filterTime = datePicker.getValue();
        if (filterTime != null)
            getFilteredProfitData(filterTime);
        else
            getFilteredProfitData();
    }

    @FXML
    void clearAction(ActionEvent event) {
        datePicker.setValue(null);
    }

    @FXML
    void openIncomeAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/fathi/newrootacademymanager/views/incomes/incomes-view.fxml"));
            pane.getChildren().clear();
            pane.getChildren().add(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void openExpenseAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/fathi/newrootacademymanager/views/expenses/expenses-view.fxml"));
            pane.getChildren().clear();
            pane.getChildren().add(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void getTotalProfitData() {
        getProfitData(totalIncomeLabel, totalExpenseLabel, totalProfitLabel);
    }

    private void getFilteredProfitData() {
        getProfitData(filteredIncomeLabel, filteredExpenseLabel, filteredProfitLabel);
    }

    private void getFilteredProfitData(LocalDate filterTime) {
        BigDecimal filteredIncome = CalculationsService.gte(BigDecimal.class, Income.class, "amount", "createTime", filterTime);
        BigDecimal filteredExpense = CalculationsService.gte(BigDecimal.class, Expense.class, "amount", "createTime", filterTime);
        BigDecimal filteredProfit = filteredIncome.subtract(filteredExpense);
        filteredIncomeLabel.setText(filteredIncome.toString());
        filteredExpenseLabel.setText(filteredExpense.toString());
        filteredProfitLabel.setText(filteredProfit.toString());
    }

    private void getProfitData(Label incomeLabel, Label expenseLabel, Label profitLabel) {
        BigDecimal totalIncome = CalculationsService.sum(BigDecimal.class, Income.class, "amount");
        BigDecimal totalExpense = CalculationsService.sum(BigDecimal.class, Expense.class, "amount");
        BigDecimal totalProfit = totalIncome.subtract(totalExpense);
        incomeLabel.setText(totalIncome.toString());
        expenseLabel.setText(totalExpense.toString());
        profitLabel.setText(totalProfit.toString());
    }

    private void fillChart() {
        XYChart.Series<String, BigDecimal> incomesData = new XYChart.Series<>();
        incomesData.setName("Incomes");
        XYChart.Series<String, BigDecimal> expensesData = new XYChart.Series<>();
        expensesData.setName("Expenses");
        Map<LocalDate, BigDecimal> incomeData = CalculationsService.getIncomeChartData(7);
        Map<LocalDate, BigDecimal> expenseData = CalculationsService.getExpenseChartData(7);
        Set<LocalDate> allDates = Stream.concat(incomeData.keySet().stream(), expenseData.keySet().stream())
                .collect(Collectors.toCollection(TreeSet::new));
        allDates.forEach(time -> {
            BigDecimal incomeAmount = incomeData.getOrDefault(time, BigDecimal.ZERO);
            BigDecimal expenseAmount = expenseData.getOrDefault(time, BigDecimal.ZERO);

            incomesData.getData().add(new XYChart.Data<>(String.valueOf(time), incomeAmount));
            expensesData.getData().add(new XYChart.Data<>(String.valueOf(time), expenseAmount.negate()));
        });
        chart.getData().add(expensesData);
        chart.getData().add(incomesData);
    }
}
