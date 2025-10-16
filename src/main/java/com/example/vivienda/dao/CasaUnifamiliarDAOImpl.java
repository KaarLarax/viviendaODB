package com.example.vivienda.dao;

import com.example.vivienda.model.CasaUnifamiliar;
import com.example.vivienda.model.SistemaCatastroDB;
import com.example.vivienda.model.Persona;
import com.example.vivienda.model.Colonia;

import javax.persistence.EntityManager;
import java.util.List;

public class CasaUnifamiliarDAOImpl implements CasaUnifamiliarDAO {
    private final SistemaCatastroDB sistema = SistemaCatastroDB.getInstance();

    private EntityManager getEntityManager() {
        return sistema.getEntityManager();
    }

    @Override
    public void create(CasaUnifamiliar casa) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();

            // Gestionar colonia
            Colonia colonia = casa.getColonia();
            if (colonia != null) {
                if (colonia.getId() != 0) {
                    colonia = em.find(Colonia.class, colonia.getId());
                }
                if (colonia == null) {
                    colonia = em.merge(casa.getColonia());
                }
                casa.setColonia(colonia);
            }

            // Gestionar propietario: intentar encontrar persona gestionada por id
            Persona propietario = casa.getPropietario();
            Persona managedPropietario = null;
            if (propietario != null) {
                if (propietario.getId() != 0) {
                    managedPropietario = em.find(Persona.class, propietario.getId());
                }
                if (managedPropietario == null) {
                    managedPropietario = em.merge(propietario);
                }
                // No establecer la vivienda del managedPropietario todavía, lo haremos después de persist
                casa.setPropietario(managedPropietario);
            }

            em.persist(casa);
            em.flush(); // Asegura que casa tenga id y esté gestionada

            // Ahora establecer la referencia inversa y merge si es necesario
            if (managedPropietario != null) {
                managedPropietario.setVivienda(casa);
                // managedPropietario ya es managed si vino de find o de merge retornó managed; solo hacer merge por seguridad
                em.merge(managedPropietario);
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

            // Gestionar colonia igual que en create
            Colonia colonia = casa.getColonia();
            if (colonia != null) {
                if (colonia.getId() != 0) {
                    colonia = em.find(Colonia.class, colonia.getId());
                }
                if (colonia == null) {
                    colonia = em.merge(casa.getColonia());
                }
                casa.setColonia(colonia);
            }

            // Gestionar propietario
            Persona propietario = casa.getPropietario();
            Persona managedPropietario = null;
            if (propietario != null) {
                if (propietario.getId() != 0) {
                    managedPropietario = em.find(Persona.class, propietario.getId());
                }
                if (managedPropietario == null) {
                    managedPropietario = em.merge(propietario);
                }
                casa.setPropietario(managedPropietario);
            }

            CasaUnifamiliar merged = em.merge(casa);
            em.flush();

            if (managedPropietario != null) {
                managedPropietario.setVivienda(merged);
                em.merge(managedPropietario);
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
