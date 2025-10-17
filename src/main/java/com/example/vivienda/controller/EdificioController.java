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

    // Crear edificio solo si la claveCatastral no existe
    public boolean crearEdificio(Edificio edificio) {
        if (claveCatastralExiste(edificio.getNumeroExterior())) {
            return false;
        }
        edificioDAO.create(edificio);
        return true;
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

    // Método auxiliar para verificar duplicados
    private boolean claveCatastralExiste(String claveCatastral) {
        return edificioDAO.readAll().stream()
                .anyMatch(e -> e.getNumeroExterior().equals(claveCatastral));
    }
}
