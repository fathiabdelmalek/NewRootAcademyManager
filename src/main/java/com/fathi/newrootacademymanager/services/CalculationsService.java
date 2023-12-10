package com.fathi.newrootacademymanager.services;

import com.fathi.newrootacademymanager.helpers.DBCManager;
import com.fathi.newrootacademymanager.models.Attendance;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CalculationsService {
    private static final DBCManager dbcm = DBCManager.getInstance();

    public static <T> int sumOfStudents(int lessonId) {
        try (EntityManager em = dbcm.getFactory().createEntityManager()) {
            String jpqlQuery = "SELECT COUNT(a) FROM Attendance a WHERE a.lesson.id = :lessonId";
            TypedQuery<Long> query = em.createQuery(jpqlQuery, Long.class);
            query.setParameter("lessonId", lessonId);
            Long result = query.getSingleResult();
            return result.intValue();
        } catch (Exception e) {
            dbcm.handleTransactionException(e);
            return -1;
        }
    }

    public static int count(String table) {
        try (EntityManager em = dbcm.getFactory().createEntityManager()) {
            String jpqlQuery = "SELECT COUNT(t) FROM " + table + " t";
            TypedQuery<Long> query = em.createQuery(jpqlQuery, Long.class);
            Long result = query.getSingleResult();
            return result.intValue();
        } catch (Exception e) {
            dbcm.handleTransactionException(e);
            return -1;
        }
    }

    public static int countStudents(int lessonId) {
        try (EntityManager em = dbcm.getFactory().createEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> query = cb.createQuery(Long.class);
            Root<Attendance> attendanceRoot = query.from(Attendance.class);
            Predicate condition = cb.equal(attendanceRoot.get("lesson").get("id"), lessonId);
            query.select(cb.count(attendanceRoot)).where(condition);
            Long result = em.createQuery(query).getSingleResult();
            return result != null ? result.intValue() : 0;
        } catch (Exception e) {
            dbcm.handleTransactionException(e);
            return -1;
        }
    }

    public static <T> double totalIncome() {
        try (EntityManager em = dbcm.getFactory().createEntityManager()) {
            String jpqlQuery = "SELECT SUM(i.amount) FROM Income i";
            TypedQuery<Double> query = em.createQuery(jpqlQuery, Double.class);
            Double result = query.getSingleResult();
            return result != null ? result : 0.0;
        } catch (Exception e) {
            dbcm.handleTransactionException(e);
            return -1;
        }
    }

    public static <T> double totalExpense() {
        try (EntityManager em = dbcm.getFactory().createEntityManager()) {
            String jpqlQuery = "SELECT SUM(e.amount) FROM Expense e";
            TypedQuery<Double> query = em.createQuery(jpqlQuery, Double.class);
            Double result = query.getSingleResult();
            return result != null ? result : 0.0;
        } catch (Exception e) {
            dbcm.handleTransactionException(e);
            return -1;
        }
    }

    public static double sum(String column, String table) {
        try (EntityManager em = dbcm.getFactory().createEntityManager()) {
            String jpqlQuery = "SELECT SUM(t." + column + ") FROM" + table + " t";
            TypedQuery<Double> query = em.createQuery(jpqlQuery, Double.class);
            Double result = query.getSingleResult();
            return result != null ? result : 0.0;
        } catch (Exception e) {
            dbcm.handleTransactionException(e);
            return -1;
        }
    }

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

    public static <T> BigDecimal sum(Class<T> entityClass, String column) {
        try (EntityManager em = dbcm.getFactory().createEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<BigDecimal> query = cb.createQuery(BigDecimal.class);
            Root<T> root = query.from(entityClass);
            query.select(cb.sum(root.get(column)));
            BigDecimal result = em.createQuery(query).getSingleResult();
            return result != null ? result : BigDecimal.ZERO;
        } catch (Exception e) {
            dbcm.handleTransactionException(e);
            return BigDecimal.ZERO;
        }
    }
}
