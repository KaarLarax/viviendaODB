package com.example.vivienda.controller;

import com.example.vivienda.dao.EdificioDAO;
import com.example.vivienda.dao.EdificioDAOImpl;
import com.example.vivienda.model.Edificio;
import java.util.List;

public class EdificioController {
    private final EdificioDAO edificioDAO;

    public EdificioController() {
        this.edificioDAO = new EdificioDAOImpl();
    }

    public void crearEdificio(Edificio edificio) {
        edificioDAO.create(edificio);
    }

    public Edificio obtenerEdificio(Long id) {
        return edificioDAO.read(id);
    }

    public List<Edificio> obtenerTodosLosEdificios() {
        return edificioDAO.readAll();
    }

    public void actualizarEdificio(Edificio edificio) {
        edificioDAO.update(edificio);
    }

    public void eliminarEdificio(Edificio edificio) {
        edificioDAO.delete(edificio);
    }

    public void eliminarEdificioPorId(Long id) {
        edificioDAO.deleteById(id);
    }
}

