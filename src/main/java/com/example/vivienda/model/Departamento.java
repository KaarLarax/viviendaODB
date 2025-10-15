package com.example.vivienda.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Departamento extends Vivienda {

    private String numero;

    @ManyToOne
    private Edificio edificio;

    public Departamento() {
        super();
    }

    public Departamento(String direccion, double superficie, String claveCatastral, Persona propietario, Colonia colonia, String numero) {
        super(direccion, superficie, claveCatastral, propietario, colonia);
        this.numero = numero;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Edificio getEdificio() {
        return edificio;
    }

    public void setEdificio(Edificio edificio) {
        this.edificio = edificio;
    }
}

