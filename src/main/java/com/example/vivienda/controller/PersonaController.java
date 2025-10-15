package com.example.vivienda.controller;

import com.example.vivienda.dao.PersonaDAO;
import com.example.vivienda.dao.PersonaDAOImpl;
import com.example.vivienda.model.Persona;
import java.util.List;

public class PersonaController {
    private final PersonaDAO personaDAO;

    public PersonaController() {
        this.personaDAO = new PersonaDAOImpl();
    }

    public void crearPersona(Persona persona) {
        personaDAO.create(persona);
    }

    public Persona obtenerPersona(Long id) {
        return personaDAO.read(id);
    }

    public List<Persona> obtenerTodasLasPersonas() {
        return personaDAO.readAll();
    }

    public void actualizarPersona(Persona persona) {
        personaDAO.update(persona);
    }

    public void eliminarPersona(Persona persona) {
        personaDAO.delete(persona);
    }

    public void eliminarPersonaPorId(Long id) {
        personaDAO.deleteById(id);
    }
}

