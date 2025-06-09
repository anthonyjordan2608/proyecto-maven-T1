package com.empresa.videoclub.repository;

import com.empresa.videoclub.model.Pelicula;
import com.empresa.videoclub.util.JPAUtil;

import jakarta.persistence.EntityManager;
import java.util.List;

public class PeliculaRepository {
    public Pelicula findById(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Pelicula.class, id);
        } finally {
            em.close();
        }
    }

    public List<Pelicula> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("FROM Pelicula", Pelicula.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void save(Pelicula pelicula) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(pelicula);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void update(Pelicula pelicula) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(pelicula);
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
            Pelicula pelicula = em.find(Pelicula.class, id);
            if (pelicula != null) {
                em.getTransaction().begin();
                em.remove(pelicula);
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
