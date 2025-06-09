package com.empresa.videoclub.service;

import java.math.BigDecimal;

import com.empresa.videoclub.model.Alquiler;
import com.empresa.videoclub.model.DetalleAlquiler;
import com.empresa.videoclub.model.DetalleAlquilerID;
import com.empresa.videoclub.model.Pelicula;
import com.empresa.videoclub.repository.AlquilerRepository;
import com.empresa.videoclub.repository.DetalleAlquilerRepository;
import com.empresa.videoclub.util.JPAUtil;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Map;

public class AlquilerService {
    private final AlquilerRepository alquilerRepository = new AlquilerRepository();
    private final PeliculaService peliculaService = new PeliculaService();
    private final DetalleAlquilerRepository detalleRepository = new DetalleAlquilerRepository();
    
    public void registrarAlquiler(Alquiler alquiler, Map<Integer, Integer> peliculasCantidad) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            
            // Calcular total
            BigDecimal total = BigDecimal.ZERO;
            for (Map.Entry<Integer, Integer> entry : peliculasCantidad.entrySet()) {
                Pelicula pelicula = em.find(Pelicula.class, entry.getKey());
                BigDecimal precio = pelicula.getGenero().equals("Nuevo") 
                    ? new BigDecimal("5.00") 
                    : new BigDecimal("3.00");
                total = total.add(precio.multiply(new BigDecimal(entry.getValue())));
            }
            alquiler.setTotal(total.doubleValue());
            
            // Guardar alquiler
            em.persist(alquiler);
            
            // Guardar detalles y actualizar stock
            for (Map.Entry<Integer, Integer> entry : peliculasCantidad.entrySet()) {
                Pelicula pelicula = em.find(Pelicula.class, entry.getKey());
                
                DetalleAlquiler detalle = new DetalleAlquiler();
                DetalleAlquilerID id = new DetalleAlquilerID();
                id.setIdAlquiler(alquiler.getIdAlquiler());
                id.setIdPelicula(pelicula.getIdPelicula());
                
                detalle.setId(id);
                detalle.setAlquiler(alquiler);
                detalle.setPelicula(pelicula);
                detalle.setCantidad(entry.getValue());
                
                em.persist(detalle);
                
                // Actualizar stock
                pelicula.setStock(pelicula.getStock() - entry.getValue());
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
    
    public List<Alquiler> listarTodos() {
        return alquilerRepository.findAll();
    }
    
    public Alquiler buscarPorId(Integer id) {
        return alquilerRepository.findById(id);
    }
}
