package com.dwgabo.dsw02practica01.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "empleados")
public class Empleado {

    @EmbeddedId
    private EmpleadoId id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre debe tener como máximo 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 100, message = "La dirección debe tener como máximo 100 caracteres")
    @Column(name = "direccion", nullable = false, length = 100)
    private String direccion;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 100, message = "El teléfono debe tener como máximo 100 caracteres")
    @Column(name = "telefono", nullable = false, length = 100)
    private String telefono;

    @Column(name = "correo", nullable = false, length = 150)
    private String correo;

    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    public EmpleadoId getId() {
        return id;
    }

    public void setId(EmpleadoId id) {
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
