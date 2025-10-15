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
        em.getTransaction().begin();
        em.persist(departamento);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void actualizarDepartamento(Departamento departamento) {
        EntityManager em = sistemaCatastroDB.getEntityManager();
        em.getTransaction().begin();
        em.merge(departamento);
        em.getTransaction().commit();
        em.close();
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
        TypedQuery<Departamento> query = em.createQuery("SELECT d FROM Departamento d", Departamento.class);
        List<Departamento> departamentos = query.getResultList();
        em.close();
        return departamentos;
    }
}

