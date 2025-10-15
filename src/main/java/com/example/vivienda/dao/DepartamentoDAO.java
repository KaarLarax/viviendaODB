package com.example.vivienda.dao;

import com.example.vivienda.model.Departamento;

import java.util.List;

public interface DepartamentoDAO {
    void agregarDepartamento(Departamento departamento);
    void actualizarDepartamento(Departamento departamento);
    void eliminarDepartamento(int id);
    Departamento obtenerDepartamentoPorId(int id);
    List<Departamento> obtenerTodosLosDepartamentos();
}

