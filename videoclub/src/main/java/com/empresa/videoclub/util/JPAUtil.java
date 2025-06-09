package com.empresa.videoclub.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
     private static final EntityManagerFactory EMF = 
        Persistence.createEntityManagerFactory("videoclubPU");

    private JPAUtil() {} // Constructor privado

    public static EntityManager getEntityManager() {
        return EMF.createEntityManager();
    }

    public static void shutdown() {
        if (EMF != null && EMF.isOpen()) {
            EMF.close();
        }
    }
}
