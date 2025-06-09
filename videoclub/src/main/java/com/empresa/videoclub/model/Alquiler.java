package com.empresa.videoclub.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.empresa.videoclub.enums.EstadoAlquiler;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name = "alquileres")
public class Alquiler implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alquiler")
    private Integer idAlquiler;
    
    @Column(nullable = false)
    private LocalDateTime fecha;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @NotNull
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private EstadoAlquiler estado = EstadoAlquiler.ACTIVO;

    @OneToMany(mappedBy = "alquiler", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DetalleAlquiler> detalles = new HashSet<>();

    public Integer getIdAlquiler() {
        return idAlquiler;
    }

    public void setIdAlquiler(Integer idAlquiler) {
        this.idAlquiler = idAlquiler;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = BigDecimal.valueOf(total);
    }

    public EstadoAlquiler getEstado() {
        return estado;
    }

    public void setEstado(EstadoAlquiler estado) {
        this.estado = estado;
    }

    public Set<DetalleAlquiler> getDetalles() {
        return detalles;
    }

    public void setDetalles(Set<DetalleAlquiler> detalles) {
        this.detalles = detalles;
    }

    
     public void addDetalle(Pelicula pelicula, int cantidad) {
        DetalleAlquiler detalle = new DetalleAlquiler();
        DetalleAlquilerID id = new DetalleAlquilerID();
        id.setIdAlquiler(this.idAlquiler);
        id.setIdPelicula(pelicula.getIdPelicula());

        detalle.setId(id);
        detalle.setAlquiler(this);
        detalle.setPelicula(pelicula);
        detalle.setCantidad(cantidad);

        detalles.add(detalle);
    }

}
