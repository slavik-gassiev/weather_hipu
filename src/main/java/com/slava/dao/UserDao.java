package com.slava.dao;

import com.slava.entity.User;
import com.slava.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.Optional;

public class UserDao {
    public void save(User user) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(user);
            transaction.commit();
        } finally {
            em.close();
        }
    }

    public Optional<User> findByUsername(String username) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("FROM User WHERE userName = :username", User.class)
                    .setParameter("username", username)
                    .getResultList()
                    .stream()
                    .findFirst();
        } finally {
            em.close();
        }
    }

    public boolean isUsernameTaken(String username) {
        return findByUsername(username).isPresent();
    }
}
