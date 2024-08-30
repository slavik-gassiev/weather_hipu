package com.slava.dao;

import com.slava.entity.Session;
import com.slava.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.Optional;

public class SessionDao {
    public void save(Session sessionEntity) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(sessionEntity);
            transaction.commit();
        } finally {
            em.close();
        }
    }

    public Optional<Session> findByToken(String sessionToken) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("FROM Session WHERE sessionToken = :sessionToken", Session.class)
                    .setParameter("sessionToken", sessionToken)
                    .getResultList()
                    .stream()
                    .findFirst();
        } finally {
            em.close();
        }
    }

    public void deleteByToken(String sessionToken) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            findByToken(sessionToken).ifPresent(em::remove);
            transaction.commit();
        } finally {
            em.close();
        }
    }
}
