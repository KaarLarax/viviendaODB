package com.example.vivienda.dao;

import com.example.vivienda.model.Colonia;

public interface ColoniaDAO {
    void create(Colonia colonia);
    Colonia read(Long id);
    java.util.List<Colonia> readAll();
    void update(Colonia colonia);
    void delete(Colonia colonia);
    void deleteById(Long id);
}

