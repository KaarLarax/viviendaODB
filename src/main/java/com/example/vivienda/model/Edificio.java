package com.example.vivienda.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Edificio extends Vivienda {
    private int totalDepartamentos;

    @OneToMany(mappedBy = "edificio", cascade = CascadeType.ALL)
    private List<Departamento> departamentos;

    public Edificio() {
        super();
        this.departamentos = new ArrayList<>();
    }

    public Edificio(String direccion, double superficie, String claveCatastral, Persona propietario, Colonia colonia, int totalDepartamentos) {
        super(direccion, superficie, claveCatastral, propietario, colonia);
        this.totalDepartamentos = totalDepartamentos;
        this.departamentos = new ArrayList<>();
    }

    public int getTotalDepartamentos() {
        return totalDepartamentos;
    }

    public void setTotalDepartamentos(int totalDepartamentos) {
        this.totalDepartamentos = totalDepartamentos;
    }

    public List<Departamento> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(List<Departamento> departamentos) {
        this.departamentos = departamentos;
    }
    public void agregarDepartamento(Departamento departamento) {
        this.departamentos.add(departamento);
        departamento.setEdificio(this);
    }

    @Override
    public String toString() {
        return getDireccion();
    }
}