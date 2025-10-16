package com.example.vivienda.dao;

import com.example.vivienda.model.Persona;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class PersonaDAOImpl implements PersonaDAO {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("objectdb:db/vivienda.odb");

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public void create(Persona persona) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(persona);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Persona read(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Persona.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Persona> readAll() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Persona p", Persona.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Persona persona) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(persona);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Persona persona) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Persona managed = em.find(Persona.class, persona.getId());
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
            Persona managed = em.find(Persona.class, id);
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
    public int getNumeroDePropiedades(Persona persona) {
        EntityManager em = getEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(v) FROM Vivienda v WHERE v.propietario = :persona", Long.class)
                    .setParameter("persona", persona)
                    .getSingleResult();
            return count.intValue();
        } finally {
            em.close();
        }
    }

    @Override
    public Persona findJefeDeFamiliaByApellidos(String apellidos) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Persona p WHERE p.apellidos = :apellidos AND p.esJefeDeFamilia = true", Persona.class)
                    .setParameter("apellidos", apellidos)
                    .getResultStream().findFirst().orElse(null);
        } finally {
            em.close();
        }
    }
}
