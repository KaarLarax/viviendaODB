package com.example.vivienda.dao;

import com.example.vivienda.model.Edificio;
import com.example.vivienda.model.SistemaCatastroDB;

import javax.persistence.EntityManager;
import java.util.List;

public class EdificioDAOImpl implements EdificioDAO {
    private final SistemaCatastroDB sistema = SistemaCatastroDB.getInstance();

    private EntityManager getEntityManager() {
        return sistema.getEntityManager();
    }

    @Override
    public void create(Edificio edificio) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();

            // Si el edificio tuviera relaciones a otras entidades, asegurarlas aquí (por ejemplo colonia)
            em.persist(edificio);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Edificio read(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Edificio.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Edificio> readAll() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT e FROM Edificio e", Edificio.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Edificio edificio) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(edificio);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Edificio edificio) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Edificio managed = em.find(Edificio.class, edificio.getId());
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
            Edificio managed = em.find(Edificio.class, id);
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
