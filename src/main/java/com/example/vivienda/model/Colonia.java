package com.example.vivienda.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Colonia {
    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true) // El nombre de la colonia es único [cite: 11]
    private String nombre;
    private String codigoPostal;

    @OneToMany(mappedBy = "colonia", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Vivienda> viviendas;

    public Colonia() {
        this.viviendas = new ArrayList<>();
    }

    public Colonia(String nombre, String codigoPostal) {
        this();
        this.nombre = nombre;
        this.codigoPostal = codigoPostal;
    }

    // Getters y Setters...
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Vivienda> getViviendas() {
        return viviendas;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setViviendas(List<Vivienda> viviendas) {
        this.viviendas = viviendas;
    }

    // Método para obtener el número de viviendas
    public int getNumeroViviendas() {
        if (viviendas == null) {
            return 0;
        }
        // Contar todas las viviendas (Edificios, CasasUnifamiliares y Departamentos)
        return viviendas.size();
    }

    @Override
    public String toString() {
        return nombre;
    }
}