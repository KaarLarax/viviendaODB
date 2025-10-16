package com.example.vivienda.controller;

import com.example.vivienda.dao.FamiliaDAO;
import com.example.vivienda.dao.FamiliaDAOImpl;
import com.example.vivienda.model.Familia;

import java.util.List;

public class FamiliaController {
    private final FamiliaDAO familiaDAO;

    public FamiliaController() {
        this.familiaDAO = new FamiliaDAOImpl();
    }

    public void crearFamilia(Familia familia) {
        familiaDAO.create(familia);
    }

    public Familia obtenerFamilia(Long id) {
        return familiaDAO.read(id);
    }

    public List<Familia> obtenerTodasLasFamilias() {
        return familiaDAO.readAll();
    }

    public void actualizarFamilia(Familia familia) {
        familiaDAO.update(familia);
    }

    public void eliminarFamilia(Familia familia) {
        familiaDAO.delete(familia);
    }

    public void eliminarFamiliaPorId(Long id) {
        familiaDAO.deleteById(id);
    }

    public Familia buscarPorApellidos(String apellidos) {
        return familiaDAO.findByApellidos(apellidos);
    }

    public int obtenerNumeroMiembros(Familia familia) {
        return familiaDAO.getNumeroMiembros(familia);
    }
}

