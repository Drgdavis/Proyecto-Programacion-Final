package com.prograiv.importaciones.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "importaciones")
public class Importacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String archivo;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false, length = 30)
    private String estado;

    @Column(nullable = false)
    private int total;

    @Column(nullable = false)
    private int aceptados;

    @Column(nullable = false)
    private int rechazados;

    protected Importacion() {
    }

    public Importacion(String archivo) {
        this.archivo = archivo;
        this.fecha = LocalDateTime.now();
        this.estado = "PROCESANDO";
    }

    public void completar(int total, int aceptados, int rechazados) {
        if (total < 0 || aceptados < 0 || rechazados < 0
                || (long) aceptados + rechazados != total) {
            throw new IllegalArgumentException(
                    "Los contadores de la importacion no coinciden.");
        }

        this.total = total;
        this.aceptados = aceptados;
        this.rechazados = rechazados;
        this.estado = "COMPLETADA";
    }

    public void marcarFallida() {
        this.estado = "FALLIDA";
    }

    public Long getId() {
        return id;
    }

    public String getArchivo() {
        return archivo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getEstado() {
        return estado;
    }

    public int getTotal() {
        return total;
    }

    public int getAceptados() {
        return aceptados;
    }

    public int getRechazados() {
        return rechazados;
    }
}