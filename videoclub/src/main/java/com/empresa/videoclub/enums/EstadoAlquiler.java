package com.empresa.videoclub.enums;

public enum EstadoAlquiler {
    ACTIVO("Activo"),
    DEVUELTO("Devuelto"),
    RETRASADO("Retrasado");

    private final String descripcion;

    EstadoAlquiler(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}