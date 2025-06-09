package com.empresa.videoclub.repository;

import com.empresa.videoclub.model.Cliente;
import com.empresa.videoclub.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;


public class ClienteRepository {
    public Cliente findById(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public List<Cliente> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("FROM Cliente", Cliente.class).getResultList();
        } finally {
            em.close();
        }
    }
}
