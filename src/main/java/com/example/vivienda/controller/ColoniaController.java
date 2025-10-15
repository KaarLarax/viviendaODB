package com.example.vivienda.controller;

import com.example.vivienda.dao.ColoniaDAO;
import com.example.vivienda.dao.ColoniaDAOImpl;
import com.example.vivienda.model.Colonia;
import java.util.List;

public class ColoniaController {
    private final ColoniaDAO coloniaDAO;

    public ColoniaController() {
        this.coloniaDAO = new ColoniaDAOImpl();
    }

    public void crearColonia(Colonia colonia) {
        coloniaDAO.create(colonia);
    }

    public Colonia obtenerColonia(Long id) {
        return coloniaDAO.read(id);
    }

    public List<Colonia> obtenerTodasLasColonias() {
        return coloniaDAO.readAll();
    }

    public void actualizarColonia(Colonia colonia) {
        coloniaDAO.update(colonia);
    }

    public void eliminarColonia(Colonia colonia) {
        coloniaDAO.delete(colonia);
    }

    public void eliminarColoniaPorId(Long id) {
        coloniaDAO.deleteById(id);
    }
}

