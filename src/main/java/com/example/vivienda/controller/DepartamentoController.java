package com.example.vivienda.controller;

import com.example.vivienda.dao.DepartamentoDAO;
import com.example.vivienda.dao.DepartamentoDAOImpl;
import com.example.vivienda.model.Departamento;

import java.util.List;

public class DepartamentoController {

    private final DepartamentoDAO departamentoDAO;

    public DepartamentoController() {
        this.departamentoDAO = new DepartamentoDAOImpl();
    }

    public void agregarDepartamento(Departamento departamento) {
        departamentoDAO.agregarDepartamento(departamento);
    }

    public void actualizarDepartamento(Departamento departamento) {
        departamentoDAO.actualizarDepartamento(departamento);
    }

    public void eliminarDepartamento(int id) {
        departamentoDAO.eliminarDepartamento(id);
    }

    public Departamento obtenerDepartamentoPorId(int id) {
        return departamentoDAO.obtenerDepartamentoPorId(id);
    }

    public List<Departamento> obtenerTodosLosDepartamentos() {
        return departamentoDAO.obtenerTodosLosDepartamentos();
    }
}

