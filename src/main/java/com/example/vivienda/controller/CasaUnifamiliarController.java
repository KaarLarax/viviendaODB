package com.example.vivienda.controller;

import com.example.vivienda.dao.CasaUnifamiliarDAO;
import com.example.vivienda.dao.CasaUnifamiliarDAOImpl;
import com.example.vivienda.model.CasaUnifamiliar;
import java.util.List;

public class CasaUnifamiliarController {
    private final CasaUnifamiliarDAO casaDAO;

    public CasaUnifamiliarController() {
        this.casaDAO = new CasaUnifamiliarDAOImpl();
    }

    public void crearCasa(CasaUnifamiliar casa) {
        casaDAO.create(casa);
    }

    public CasaUnifamiliar obtenerCasa(Long id) {
        return casaDAO.read(id);
    }

    public List<CasaUnifamiliar> obtenerTodasLasCasas() {
        return casaDAO.readAll();
    }

    public void actualizarCasa(CasaUnifamiliar casa) {
        casaDAO.update(casa);
    }

    public void eliminarCasa(CasaUnifamiliar casa) {
        casaDAO.delete(casa);
    }

    public void eliminarCasaPorId(Long id) {
        casaDAO.deleteById(id);
    }
}

