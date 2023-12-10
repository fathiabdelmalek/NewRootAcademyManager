package com.fathi.newrootacademymanager.services;

import com.fathi.newrootacademymanager.helpers.DBCManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CRUDService {
    private static final DBCManager dbcm = DBCManager.getInstance();

    public static <T> ObservableList<T> readAll(Class<T> entityClass) {
        try (EntityManager em = dbcm.getFactory().createEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> cr = cb.createQuery(entityClass);
            cr.from(entityClass);
            ObservableList<T> result = FXCollections.observableArrayList();
            result.addAll(em.createQuery(cr).getResultList());
            return result;
        } catch (Exception e) {
            dbcm.handleTransactionException(e);
            return null;
        }
    }

    public static <T> ObservableList<T> readByCriteria(Class<T> entityClass, Map<String, Object> params) {
        try (EntityManager em = dbcm.getFactory().createEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> query = cb.createQuery(entityClass);
            Root<T> root = query.from(entityClass);
            List<Predicate> predicates = new ArrayList<>();
            for (Map.Entry<String, Object> entry : params.entrySet())
                predicates.add(cb.equal(root.get(entry.getKey()), entry.getValue()));
            query.where(predicates.toArray(new Predicate[0]));
            return FXCollections.observableArrayList(em.createQuery(query).getResultList());
        } catch (Exception e) {
            dbcm.handleTransactionException(e);
            return null;
        }
    }

    public static <T> T readById(Class<T> entityClass, int id) {
        try (EntityManager em = dbcm.getFactory().createEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> cr = cb.createQuery(entityClass);
            Root<T> root = cr.from(entityClass);
            return em.createQuery(cr.where(cb.equal(root.get("id"), id))).getSingleResult();
        } catch (Exception e) {
            dbcm.handleTransactionException(e);
            return null;
        }
    }

    public static void create(Object entity) {
        try (EntityManager em = dbcm.getFactory().createEntityManager()) {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            dbcm.handleTransactionException(e);
        }
    }

    public static void update(Object entity) {
        try (EntityManager em = dbcm.getFactory().createEntityManager()) {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            dbcm.handleTransactionException(e);
        }
    }

    public static void delete(Object entity) {
        try (EntityManager em = dbcm.getFactory().createEntityManager()) {
            em.getTransaction().begin();
            em.remove(em.contains(entity) ? entity : em.merge(entity));
            em.getTransaction().commit();
        } catch (Exception e) {
            dbcm.handleTransactionException(e);
        }
    }
}
