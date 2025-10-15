package com.example.vivienda.dao;

import com.example.vivienda.model.CasaUnifamiliar;

public interface CasaUnifamiliarDAO {
    void create(CasaUnifamiliar casa);
    CasaUnifamiliar read(Long id);
    java.util.List<CasaUnifamiliar> readAll();
    void update(CasaUnifamiliar casa);
    void delete(CasaUnifamiliar casa);
    void deleteById(Long id);
}
