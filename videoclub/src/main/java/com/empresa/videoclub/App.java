package com.empresa.videoclub;

import com.empresa.videoclub.model.Cliente;
import com.empresa.videoclub.model.Pelicula;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
         EntityManagerFactory emf = Persistence.createEntityManagerFactory("videoclubPU");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Cliente c1 = new Cliente();
            c1.setNombre("Pedro");
            c1.setEmail("pedro@email.com");

            Pelicula p1 = new Pelicula();
            p1.setTitulo("Matrix");
            p1.setGenero("Ciencia Ficción");
            p1.setStock(10);

            em.persist(c1);
            em.persist(p1);

            em.getTransaction().commit();
        } finally {
            em.close();
            emf.close();
        }
    }
}
