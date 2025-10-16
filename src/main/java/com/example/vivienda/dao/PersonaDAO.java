package com.example.vivienda.dao;

import com.example.vivienda.model.Persona;

public interface PersonaDAO {
    void create(Persona persona);
    Persona read(Long id);
    java.util.List<Persona> readAll();
    void update(Persona persona);
    void delete(Persona persona);
    void deleteById(Long id);
    int getNumeroDePropiedades(Persona persona);
    Persona findJefeDeFamiliaByApellidos(String apellidos);
}
