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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.hibernate.cfg.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private LineChart<?, ?> chart;
    @FXML
    private DatePicker datePicker;

    @FXML
    void initialize() {
        LocalDate filterTime = LocalDate.now().minusMonths(1);
        datePicker.setValue(filterTime);
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
