package com.example.vivienda.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Familia {
    @Id @GeneratedValue
    private long id;
    private String apellidos;

    public Familia() {}

    public Familia(String apellidos) {
        this.apellidos = apellidos;
    }

    public long getId() {
        return id;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    @Override
    public String toString() {
        return apellidos;
    }
}

