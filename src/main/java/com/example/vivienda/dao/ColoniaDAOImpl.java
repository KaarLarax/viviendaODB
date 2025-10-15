package com.example.vivienda.dao;

import com.example.vivienda.model.Colonia;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class ColoniaDAOImpl implements ColoniaDAO {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("objectdb:db/vivienda.odb");

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public void create(Colonia colonia) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(colonia);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Colonia read(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Colonia.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Colonia> readAll() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Colonia c", Colonia.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Colonia colonia) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(colonia);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Colonia colonia) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Colonia managed = em.find(Colonia.class, colonia.getId());
            if (managed != null) {
                em.remove(managed);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteById(Long id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Colonia managed = em.find(Colonia.class, id);
            if (managed != null) {
                em.remove(managed);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}