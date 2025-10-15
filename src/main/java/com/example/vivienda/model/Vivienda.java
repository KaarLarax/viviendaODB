package com.example.vivienda.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
// Estrategia de herencia: todos los tipos de vivienda en una sola tabla
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Vivienda {
    @Id @GeneratedValue
    private long id;

    private String direccion;
    private double superficie;
    @Column(unique = true) // Clave catastral debe ser única
    private String claveCatastral;

    // Relación: Una vivienda tiene un y sólo un propietario [cite: 9]
    @OneToOne
    private Persona propietario;

    // Relación: Una vivienda tiene muchos habitantes [cite: 5]
    // "mappedBy" indica que la clase Persona gestiona la relación
    @OneToMany(mappedBy = "vivienda", cascade = CascadeType.PERSIST)
    private List<Persona> habitantes;
    
    // Relación: Muchas viviendas pertenecen a una colonia [cite: 10]
    @ManyToOne
    private Colonia colonia;

    public Vivienda() {
        this.habitantes = new ArrayList<>();
    }

    public Vivienda(String direccion, double superficie, String claveCatastral, Persona propietario, Colonia colonia) {
        this();
        this.direccion = direccion;
        this.superficie = superficie;
        this.claveCatastral = claveCatastral;
        this.propietario = propietario;
        this.colonia = colonia;
    }

    public void agregarHabitante(Persona habitante) {
        this.habitantes.add(habitante);
        habitante.setVivienda(this); // Mantiene la consistencia de la relación
    }

    // Getters y Setters...
    public long getId() { return id; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public Persona getPropietario() { return propietario; }
    public void setPropietario(Persona propietario) { this.propietario = propietario; }
    public List<Persona> getHabitantes() { return habitantes; }

    public void setId(long id) {
        this.id = id;
    }

    public double getSuperficie() {
        return superficie;
    }

    public void setSuperficie(double superficie) {
        this.superficie = superficie;
    }

    public String getClaveCatastral() {
        return claveCatastral;
    }

    public void setClaveCatastral(String claveCatastral) {
        this.claveCatastral = claveCatastral;
    }

    public void setHabitantes(List<Persona> habitantes) {
        this.habitantes = habitantes;
    }

    public Colonia getColonia() {
        return colonia;
    }

    public void setColonia(Colonia colonia) {
        this.colonia = colonia;
    }
}