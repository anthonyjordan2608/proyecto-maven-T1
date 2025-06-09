package com.empresa.videoclub.repository;

import com.empresa.videoclub.model.Alquiler;
import com.empresa.videoclub.util.JPAUtil;

import jakarta.persistence.EntityManager;
import java.util.List;

public class AlquilerRepository {
    public Alquiler findById(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Alquiler.class, id);
        } finally {
            em.close();
        }
    }

    public List<Alquiler> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("FROM Alquiler", Alquiler.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void save(Alquiler alquiler) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(alquiler);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void update(Alquiler alquiler) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(alquiler);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void delete(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Alquiler alquiler = em.find(Alquiler.class, id);
            if (alquiler != null) {
                em.getTransaction().begin();
                em.remove(alquiler);
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
