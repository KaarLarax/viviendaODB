package com.example.vivienda.model;

import javax.persistence.*;

@Entity
public class Persona {
    @Id @GeneratedValue
    private long id; // ID único para la base de datos

    private String nombre;
    private String rfc;
    private boolean esJefeDeFamilia;
    private int edad;
    private String apellidos;
    // Relación: Muchas personas habitan en una vivienda [cite: 8]
    @ManyToOne
    private Vivienda vivienda;

    // Constructor vacío requerido por JPA
    public Persona() {}

    public Persona(String nombre, String rfc, boolean esJefeDeFamilia,int edad, String apellidos) {
        this.nombre = nombre;
        this.rfc = rfc;
        this.esJefeDeFamilia = esJefeDeFamilia;
        this.edad = edad;
        this.apellidos = apellidos;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    // Getters y Setters...
    public long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getRfc() { return rfc; }
    public void setRfc(String rfc) { this.rfc = rfc; }
    public boolean isEsJefeDeFamilia() { return esJefeDeFamilia; }
    public void setEsJefeDeFamilia(boolean esJefeDeFamilia) { this.esJefeDeFamilia = esJefeDeFamilia; }
    public Vivienda getVivienda() { return vivienda; }
    public void setVivienda(Vivienda vivienda) { this.vivienda = vivienda; }
}