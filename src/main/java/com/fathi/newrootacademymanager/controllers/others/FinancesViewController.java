package com.fathi.newrootacademymanager.controllers.others;

import com.fathi.newrootacademymanager.models.Expense;
import com.fathi.newrootacademymanager.models.Income;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.hibernate.cfg.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FinancesViewController {
    @FXML
    private Pane filteredPane;
    @FXML
    private Pane totalPane;
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
        XYChart.Series<String, BigDecimal> incomesData = new XYChart.Series<>();
        incomesData.setName("Incomes");
        XYChart.Series<String, BigDecimal> expensesData = new XYChart.Series<>();
        expensesData.setName("Expenses");
        EntityManagerFactory factory = new Configuration().configure().buildSessionFactory();
        try (EntityManager em = factory.createEntityManager()) {

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<BigDecimal> crIncome = cb.createQuery(BigDecimal.class);
            CriteriaQuery<BigDecimal> crExpense = cb.createQuery(BigDecimal.class);
            Root<Income> rootIncome = crIncome.from(Income.class);
            Root<Expense> rootExpense = crExpense.from(Expense.class);

            // total profit
            crIncome.select(cb.sum(rootIncome.get("amount")));
            crExpense.select(cb.sum(rootExpense.get("amount")));
            BigDecimal totalIncome = em.createQuery(crIncome).getSingleResult();
            BigDecimal totalExpense = em.createQuery(crExpense).getSingleResult();
            BigDecimal profit = totalIncome.subtract(totalExpense);
            totalIncomeLabel.setText(totalIncome.toString());
            totalExpenseLabel.setText(totalExpense.toString());
            totalProfitLabel.setText(profit.toString());

            // filtered profit
            crIncome = cb.createQuery(BigDecimal.class);
            crExpense = cb.createQuery(BigDecimal.class);
            rootIncome = crIncome.from(Income.class);
            rootExpense = crExpense.from(Expense.class);

            crIncome
                    .where(cb.greaterThanOrEqualTo(rootIncome.get("createTime"), filterTime))
                    .select(cb.sum(rootIncome.get("amount")));
            crExpense
                    .where(cb.greaterThanOrEqualTo(rootExpense.get("createTime"), filterTime))
                    .select(cb.sum(rootExpense.get("amount")));
            BigDecimal filteredIncome = em.createQuery(crIncome).getSingleResult();
            BigDecimal filteredExpense = em.createQuery(crExpense).getSingleResult();
            BigDecimal filteredProfit = filteredIncome.subtract(filteredExpense);
            filteredIncomeLabel.setText(filteredIncome.toString());
            filteredExpenseLabel.setText(filteredExpense.toString());
            filteredProfitLabel.setText(filteredProfit.toString());


            CriteriaQuery<Object[]> crChartIncome = cb.createQuery(Object[].class);
            CriteriaQuery<Object[]> crChartExpense = cb.createQuery(Object[].class);
            Root<Income> rootChartIncome = crChartIncome.from(Income.class);
            Root<Expense> rootChartExpense = crChartExpense.from(Expense.class);

            crChartIncome
                    .multiselect(cb.function("date", LocalDate.class, rootChartIncome.get("createTime")), cb.sum(rootChartIncome.get("amount")))
                    .groupBy(cb.function("date", LocalDate.class, rootChartIncome.get("createTime")))
                    .orderBy(cb.desc(cb.function("date", LocalDate.class, rootChartIncome.get("createTime"))));
            crChartExpense
                    .multiselect(cb.function("date", LocalDate.class, rootChartExpense.get("createTime")), cb.sum(rootChartExpense.get("amount")))
                    .groupBy(cb.function("date", LocalDate.class, rootChartExpense.get("createTime")))
                    .orderBy(cb.desc(cb.function("date", LocalDate.class, rootChartExpense.get("createTime"))));

            List<Object[]> incomeResult = em.createQuery(crChartIncome).setMaxResults(7).getResultList();
            List<Object[]> expenseResult = em.createQuery(crChartExpense).setMaxResults(7).getResultList();

            Map<LocalDate, BigDecimal> incomeData = incomeResult.stream()
                    .collect(Collectors.groupingBy(
                            data -> ((LocalDate) data[0]),
                            Collectors.mapping(data -> (BigDecimal) data[1], Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                    ));
            Map<LocalDate, BigDecimal> expenseData = expenseResult.stream()
                    .collect(Collectors.groupingBy(
                            data -> ((LocalDate) data[0]),
                            Collectors.mapping(data -> (BigDecimal) data[1], Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                    ));

            Set<LocalDate> allDates = Stream.concat(incomeData.keySet().stream(), expenseData.keySet().stream())
                    .collect(Collectors.toCollection(TreeSet::new));
            allDates.forEach(time -> {
                BigDecimal incomeAmount = incomeData.getOrDefault(time, BigDecimal.ZERO);
                BigDecimal expenseAmount = expenseData.getOrDefault(time, BigDecimal.ZERO);

                incomesData.getData().add(new XYChart.Data<>(String.valueOf(time), incomeAmount));
                expensesData.getData().add(new XYChart.Data<>(String.valueOf(time), expenseAmount.negate()));
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        chart.getData().add(expensesData);
        chart.getData().add(incomesData);
    }

    @FXML
    void filterAction(ActionEvent event) {
        LocalDate filterTime = datePicker.getValue();
        if (filterTime != null) {
            EntityManagerFactory factory = new Configuration().configure().buildSessionFactory();
            try (EntityManager em = factory.createEntityManager()) {

                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery<BigDecimal> crIncome = cb.createQuery(BigDecimal.class);
                CriteriaQuery<BigDecimal> crExpense = cb.createQuery(BigDecimal.class);
                Root<Income> rootIncome = crIncome.from(Income.class);
                Root<Expense> rootExpense = crExpense.from(Expense.class);

                crIncome
                        .where(cb.greaterThanOrEqualTo(rootIncome.get("createTime"), filterTime))
                        .select(cb.sum(rootIncome.get("amount")));
                crExpense
                        .where(cb.greaterThanOrEqualTo(rootExpense.get("createTime"), filterTime))
                        .select(cb.sum(rootExpense.get("amount")));

                BigDecimal filteredIncome = em.createQuery(crIncome).getSingleResult();
                BigDecimal filteredExpense = em.createQuery(crExpense).getSingleResult();
                BigDecimal filteredProfit = filteredIncome.subtract(filteredExpense);
                filteredIncomeLabel.setText(filteredIncome.toString());
                filteredExpenseLabel.setText(filteredExpense.toString());
                filteredProfitLabel.setText(filteredProfit.toString());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void clearAction(ActionEvent event) {
        datePicker.setValue(null);
        EntityManagerFactory factory = new Configuration().configure().buildSessionFactory();
        try (EntityManager em = factory.createEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<BigDecimal> crIncome = cb.createQuery(BigDecimal.class);
            CriteriaQuery<BigDecimal> crExpense = cb.createQuery(BigDecimal.class);
            Root<Income> rootIncome = crIncome.from(Income.class);
            Root<Expense> rootExpense = crExpense.from(Expense.class);

            crIncome.select(cb.sum(rootIncome.get("amount")));
            crExpense.select(cb.sum(rootExpense.get("amount")));

            BigDecimal filteredIncome = em.createQuery(crIncome).getSingleResult();
            BigDecimal filteredExpense = em.createQuery(crExpense).getSingleResult();
            BigDecimal filteredProfit = filteredIncome.subtract(filteredExpense);
            filteredIncomeLabel.setText(filteredIncome.toString());
            filteredExpenseLabel.setText(filteredExpense.toString());
            filteredProfitLabel.setText(filteredProfit.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void openExpenseAction(ActionEvent event) {
    }

    @FXML
    void openIncomeAction(ActionEvent event) {
    }
}
