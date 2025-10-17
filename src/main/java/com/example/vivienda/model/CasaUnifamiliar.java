package com.example.vivienda.model;

import javax.persistence.Entity;

@Entity
public class CasaUnifamiliar extends Vivienda {
    private int numeroPisos;

    public CasaUnifamiliar() { super(); }

    public CasaUnifamiliar(String direccion, double superficie, String numeroExterior, Persona propietario, Colonia colonia, int numeroPisos) {
        super(direccion, superficie, numeroExterior, propietario, colonia);
        this.numeroPisos = numeroPisos;
    }
    // Getters y Setters...

    public int getNumeroPisos() {
        return numeroPisos;
    }

    public void setNumeroPisos(int numeroPisos) {
        this.numeroPisos = numeroPisos;
    }
}