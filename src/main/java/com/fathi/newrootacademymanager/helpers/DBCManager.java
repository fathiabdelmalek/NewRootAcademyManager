package com.fathi.newrootacademymanager.helpers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.cfg.Configuration;

public class DBCManager {
    private static DBCManager instance = null;
    private final EntityManagerFactory factory;

    private DBCManager() {
        factory = new Configuration().configure().buildSessionFactory();
    }

    public static DBCManager getInstance() {
        if (instance == null)
            instance = new DBCManager();
        return instance;
    }

    public EntityManagerFactory getFactory() {
        return factory;
    }

    public void handleTransactionException() {
        if (factory != null && factory.isOpen()) {
            EntityManager em = factory.createEntityManager();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }
}
