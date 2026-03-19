package com.dwgabo.dsw02practica01.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "departamentos")
public class Departamento {

    @EmbeddedId
    private DepartamentoId id;

    @NotBlank(message = "El nombre del departamento es obligatorio")
    @Size(max = 100, message = "El nombre del departamento debe tener como máximo 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    public DepartamentoId getId() {
        return id;
    }

    public void setId(DepartamentoId id) {
        this.id = id;
    }

    @Transient
    public String getClave() {
        if (id == null || id.getPrefijo() == null || id.getNumero() == null) {
            return null;
        }
        return id.getPrefijo() + "-" + id.getNumero();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
