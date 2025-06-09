package com.empresa.videoclub.repository;

import com.empresa.videoclub.model.DetalleAlquiler;
import com.empresa.videoclub.util.JPAUtil;

import jakarta.persistence.EntityManager;
import java.util.List;


public class DetalleAlquilerRepository {
    public void save(DetalleAlquiler detalle) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(detalle);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
    
    public List<DetalleAlquiler> findByAlquiler(Integer idAlquiler) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                "SELECT d FROM DetalleAlquiler d WHERE d.alquiler.idAlquiler = :idAlquiler", 
                DetalleAlquiler.class)
                .setParameter("idAlquiler", idAlquiler)
                .getResultList();
        } finally {
            em.close();
        }
    }
}
