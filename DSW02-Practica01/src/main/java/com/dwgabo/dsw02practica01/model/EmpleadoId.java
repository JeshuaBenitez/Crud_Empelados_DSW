package com.dwgabo.dsw02practica01.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EmpleadoId implements Serializable {

    @Column(name = "prefijo", nullable = false, length = 3)
    private String prefijo;

    @Column(name = "numero", nullable = false)
    private Long numero;

    public EmpleadoId() {
    }

    public EmpleadoId(String prefijo, Long numero) {
        this.prefijo = prefijo;
        this.numero = numero;
    }

    public String getPrefijo() {
        return prefijo;
    }

    public void setPrefijo(String prefijo) {
        this.prefijo = prefijo;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmpleadoId empleadoId)) {
            return false;
        }
        return Objects.equals(prefijo, empleadoId.prefijo) && Objects.equals(numero, empleadoId.numero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prefijo, numero);
    }
}
