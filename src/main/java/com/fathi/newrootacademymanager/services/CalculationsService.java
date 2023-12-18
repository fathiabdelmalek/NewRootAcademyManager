package com.fathi.newrootacademymanager.services;

import com.fathi.newrootacademymanager.helpers.DBCManager;
import com.fathi.newrootacademymanager.helpers.enums.Level;
import com.fathi.newrootacademymanager.models.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CalculationsService {
    private static final DBCManager dbcm = DBCManager.getInstance();

    private CalculationsService() {}

    public static <T> int count(Class<T> entityClass) {
        try (EntityManager em = dbcm.getFactory().createEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> query = cb.createQuery(Long.class);
            Root<T> root = query.from(entityClass);
            query.select(cb.count(root));
            Long result = em.createQuery(query).getSingleResult();
            return result != null ? result.intValue() : 0;
        } catch (Exception e) {
            dbcm.handleTransactionException(e);
            return -1;
        }
    }

    public static <T> int count(Class<T> entityClass, Map<String, Object> params) {
        try (EntityManager em = dbcm.getFactory().createEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> query = cb.createQuery(Long.class);
            Root<T> root = query.from(entityClass);
            List<Predicate> conditions = new ArrayList<>();
            for (Map.Entry<String, Object> entry : params.entrySet())
                conditions.add(cb.equal(root.get(entry.getKey()), entry.getValue()));
            query.select(cb.count(root)).where(conditions.toArray(new Predicate[0]));
            Long result = em.createQuery(query).getSingleResult();
            return result != null ? result.intValue() : 0;
        } catch (Exception e) {
            dbcm.handleTransactionException(e);
            return -1;
        }
    }

    public static <T, U> T sum(Class<T> fieldClass, Class<U> entityClass, String column) {
        try (EntityManager em = dbcm.getFactory().createEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> query = cb.createQuery(fieldClass);
            Root<U> root = query.from(entityClass);
            query.select((Selection<T>) cb.sum(root.get(column)));
            return em.createQuery(query).getSingleResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T, U> T gte(Class<T> fieldClass, Class<U> entityClass, String column, String filterValue, Comparable<?> operator) {
        try (EntityManager em = dbcm.getFactory().createEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> query = cb.createQuery(fieldClass);
            Root<U> root = query.from(entityClass);
            query
                    .where(cb.greaterThanOrEqualTo(root.get(filterValue), (Comparable) operator))
                    .select((Selection<T>) cb.sum(root.get(column)));
            return em.createQuery(query).getSingleResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static int getStudentsWithGrade(Grade grade) {
        try (EntityManager em = dbcm.getFactory().createEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> query = cb.createQuery(Long.class);
            Root<Student> root = query.from(Student.class);
            query.select(cb.count(root)).where(cb.equal(root.get("grade"), grade));
            return em.createQuery(query).getSingleResult().intValue();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static int getStudentsWithLevel(Level level) {
        try (EntityManager em = dbcm.getFactory().createEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> query = cb.createQuery(Long.class);
            Root<Student> root = query.from(Student.class);
            query.select(cb.count(root)).where(cb.equal(root.get("grade").get("level"), level));
            return em.createQuery(query).getSingleResult().intValue();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<LocalDate, BigDecimal> getIncomeChartData(int maxResults) {
        try (EntityManager em = dbcm.getFactory().createEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
            Root<Income> root = query.from(Income.class);
            query
                    .multiselect(cb.function("date", LocalDate.class, root.get("createTime")), cb.sum(root.get("amount")))
                    .groupBy(cb.function("date", LocalDate.class, root.get("createTime")))
                    .orderBy(cb.desc(cb.function("date", LocalDate.class, root.get("createTime"))));
            return em.createQuery(query).setMaxResults(maxResults).getResultList().stream()
                    .collect(Collectors.groupingBy(
                            data -> ((LocalDate) data[0]),
                            Collectors.mapping(data -> (BigDecimal) data[1], Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                    ));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<LocalDate, BigDecimal> getExpenseChartData(int maxResults) {
        try (EntityManager em = dbcm.getFactory().createEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
            Root<Expense> root = query.from(Expense.class);
            query
                    .multiselect(cb.function("date", LocalDate.class, root.get("createTime")), cb.sum(root.get("amount")))
                    .groupBy(cb.function("date", LocalDate.class, root.get("createTime")))
                    .orderBy(cb.desc(cb.function("date", LocalDate.class, root.get("createTime"))));
            return em.createQuery(query).setMaxResults(maxResults).getResultList().stream()
                    .collect(Collectors.groupingBy(
                            data -> ((LocalDate) data[0]),
                            Collectors.mapping(data -> (BigDecimal) data[1], Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                    ));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
