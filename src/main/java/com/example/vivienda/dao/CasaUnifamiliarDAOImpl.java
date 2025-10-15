package com.example.vivienda.dao;

import com.example.vivienda.model.CasaUnifamiliar;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class CasaUnifamiliarDAOImpl implements CasaUnifamiliarDAO {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("objectdb:db/vivienda.odb");

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public void create(CasaUnifamiliar casa) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(casa);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public CasaUnifamiliar read(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CasaUnifamiliar.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<CasaUnifamiliar> readAll() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT c FROM CasaUnifamiliar c", CasaUnifamiliar.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void update(CasaUnifamiliar casa) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(casa);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(CasaUnifamiliar casa) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            CasaUnifamiliar managed = em.find(CasaUnifamiliar.class, casa.getId());
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
            CasaUnifamiliar managed = em.find(CasaUnifamiliar.class, id);
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
