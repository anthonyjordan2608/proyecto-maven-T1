package com.empresa.videoclub.service;

import java.util.stream.Collectors;

import com.empresa.videoclub.model.Pelicula;
import com.empresa.videoclub.repository.PeliculaRepository;
import com.empresa.videoclub.util.JPAUtil;

import jakarta.persistence.EntityManager;
import java.util.List;

public class PeliculaService {
    private final PeliculaRepository peliculaRepository = new PeliculaRepository();
    
    public List<Pelicula> listarPeliculasDisponibles() {
        return peliculaRepository.findAll().stream()
            .filter(p -> p.getStock() > 0)
            .collect(Collectors.toList());
    }
    
    public Pelicula buscarPorId(Integer id) {
        return peliculaRepository.findById(id);
    }
    
    public void actualizarStock(Integer idPelicula, int cantidad) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Pelicula pelicula = em.find(Pelicula.class, idPelicula);
            if (pelicula != null) {
                pelicula.setStock(pelicula.getStock() - cantidad);
                em.merge(pelicula);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
