package com.example.vivienda.model;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class SistemaCatastroDB {

    private static SistemaCatastroDB instance;
    private final EntityManagerFactory emf;

    private SistemaCatastroDB() {
        emf = Persistence.createEntityManagerFactory("vivienda-db");
    }

    public static SistemaCatastroDB getInstance() {
        if (instance == null) {
            instance = new SistemaCatastroDB();
        }
        return instance;
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void close() {
        emf.close();
    }
}