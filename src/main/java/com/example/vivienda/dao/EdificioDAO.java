package com.example.vivienda.dao;

import com.example.vivienda.model.Edificio;

public interface EdificioDAO {
    void create(Edificio edificio);
    Edificio read(Long id);
    java.util.List<Edificio> readAll();
    void update(Edificio edificio);
    void delete(Edificio edificio);
    void deleteById(Long id);
}

