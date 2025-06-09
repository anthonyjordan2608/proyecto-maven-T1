package com.empresa.videoclub.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;


@Embeddable
public class DetalleAlquilerID implements Serializable{
    private Integer idAlquiler;
    private Integer idPelicula;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idAlquiler == null) ? 0 : idAlquiler.hashCode());
        result = prime * result + ((idPelicula == null) ? 0 : idPelicula.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DetalleAlquilerID other = (DetalleAlquilerID) obj;
        if (idAlquiler == null) {
            if (other.idAlquiler != null)
                return false;
        } else if (!idAlquiler.equals(other.idAlquiler))
            return false;
        if (idPelicula == null) {
            if (other.idPelicula != null)
                return false;
        } else if (!idPelicula.equals(other.idPelicula))
            return false;
        return true;
    }
    public DetalleAlquilerID(Integer idAlquiler, Integer idPelicula) {
        this.idAlquiler = idAlquiler;
        this.idPelicula = idPelicula;
    }

    public DetalleAlquilerID(){

    }
    public Integer getIdAlquiler() {
        return idAlquiler;
    }
    public void setIdAlquiler(Integer idAlquiler) {
        this.idAlquiler = idAlquiler;
    }
    public Integer getIdPelicula() {
        return idPelicula;
    }
    public void setIdPelicula(Integer idPelicula) {
        this.idPelicula = idPelicula;
    }
    
}

