package com.example.vivienda.dao;

import com.example.vivienda.model.Familia;

import java.util.List;

public interface FamiliaDAO {
    void create(Familia familia);
    Familia read(Long id);
    List<Familia> readAll();
    void update(Familia familia);
    void delete(Familia familia);
    void deleteById(Long id);
    Familia findByApellidos(String apellidos);
    int getNumeroMiembros(Familia familia);
}

