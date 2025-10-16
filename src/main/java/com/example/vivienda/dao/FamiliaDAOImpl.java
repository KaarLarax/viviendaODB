package com.example.vivienda.dao;

import com.example.vivienda.model.Familia;
import com.example.vivienda.model.SistemaCatastroDB;

import javax.persistence.EntityManager;
import java.util.List;

public class FamiliaDAOImpl implements FamiliaDAO {
    private final SistemaCatastroDB sistema = SistemaCatastroDB.getInstance();

    private EntityManager getEntityManager() {
        return sistema.getEntityManager();
    }

    @Override
    public void create(Familia familia) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(familia);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Familia read(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Familia.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Familia> readAll() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT f FROM Familia f", Familia.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Familia familia) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(familia);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Familia familia) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Familia managed = em.find(Familia.class, familia.getId());
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
            Familia managed = em.find(Familia.class, id);
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
    public Familia findByApellidos(String apellidos) {
        EntityManager em = getEntityManager();
        try {
            List<Familia> resultados = em.createQuery(
                            "SELECT f FROM Familia f WHERE f.apellidos = :apellidos",
                            Familia.class)
                    .setParameter("apellidos", apellidos)
                    .getResultList();

            return resultados.isEmpty() ? null : resultados.get(0);
        } finally {
            em.close();
        }
    }

    @Override
    public int getNumeroMiembros(Familia familia) {
        EntityManager em = getEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(p) FROM Persona p WHERE p.familia = :familia",
                            Long.class)
                    .setParameter("familia", familia)
                    .getSingleResult();
            return count.intValue();
        } finally {
            em.close();
        }
    }
}

