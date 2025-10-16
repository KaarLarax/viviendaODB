package com.example.vivienda.dao;

import com.example.vivienda.model.Departamento;
import com.example.vivienda.model.SistemaCatastroDB;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class DepartamentoDAOImpl implements DepartamentoDAO {

    private final SistemaCatastroDB sistemaCatastroDB = SistemaCatastroDB.getInstance();

    @Override
    public void agregarDepartamento(Departamento departamento) {
        EntityManager em = sistemaCatastroDB.getEntityManager();
        try {
            em.getTransaction().begin();

            // Hacer merge de las entidades relacionadas para asegurar que estén gestionadas
            if (departamento.getEdificio() != null) {
                departamento.setEdificio(em.merge(departamento.getEdificio()));
            }
            if (departamento.getColonia() != null) {
                departamento.setColonia(em.merge(departamento.getColonia()));
            }
            if (departamento.getPropietario() != null) {
                departamento.setPropietario(em.merge(departamento.getPropietario()));
            }

            em.persist(departamento);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void actualizarDepartamento(Departamento departamento) {
        EntityManager em = sistemaCatastroDB.getEntityManager();
        try {
            em.getTransaction().begin();

            // Asegurar que las entidades relacionadas estén gestionadas antes de merge del departamento
            if (departamento.getEdificio() != null) {
                departamento.setEdificio(em.merge(departamento.getEdificio()));
            }
            if (departamento.getColonia() != null) {
                departamento.setColonia(em.merge(departamento.getColonia()));
            }
            if (departamento.getPropietario() != null) {
                departamento.setPropietario(em.merge(departamento.getPropietario()));
            }

            em.merge(departamento);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void eliminarDepartamento(int id) {
        EntityManager em = sistemaCatastroDB.getEntityManager();
        em.getTransaction().begin();
        Departamento departamento = em.find(Departamento.class, id);
        if (departamento != null) {
            em.remove(departamento);
        }
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Departamento obtenerDepartamentoPorId(int id) {
        EntityManager em = sistemaCatastroDB.getEntityManager();
        Departamento departamento = em.find(Departamento.class, id);
        em.close();
        return departamento;
    }

    @Override
    public List<Departamento> obtenerTodosLosDepartamentos() {
        EntityManager em = sistemaCatastroDB.getEntityManager();
        TypedQuery<Departamento> query = em.createQuery(
            "SELECT d FROM Departamento d " +
            "LEFT JOIN FETCH d.edificio " +
            "LEFT JOIN FETCH d.colonia " +
            "LEFT JOIN FETCH d.propietario " +
            "LEFT JOIN FETCH d.propietario.familia",
            Departamento.class);
        List<Departamento> departamentos = query.getResultList();
        em.close();
        return departamentos;
    }
}
