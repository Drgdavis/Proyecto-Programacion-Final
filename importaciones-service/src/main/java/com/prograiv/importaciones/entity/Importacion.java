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

    @Column(columnDefinition = "TEXT")
    private String mensaje;

    protected Importacion() {
    }

    public Importacion(String archivo) {
        this.archivo = archivo;
        this.fecha = LocalDateTime.now();
        this.estado = "PROCESANDO";
    }

    public void preparar(int total, int rechazados) {
        if (total < 0 || rechazados < 0 || rechazados > total) {
            throw new IllegalArgumentException("Contadores incorrectos.");
        }

        this.total = total;
        this.rechazados = rechazados;
    }

    public void completar(int aceptados, int rechazados) {
        if (aceptados < 0 || rechazados < 0
                || (long) aceptados + rechazados != total) {
            throw new IllegalArgumentException(
                    "Los contadores de la importacion no coinciden.");
        }

        this.aceptados = aceptados;
        this.rechazados = rechazados;
        this.estado = rechazados == 0
                ? "COMPLETADA"
                : "COMPLETADA_CON_ERRORES";
        this.mensaje = null;
    }

    public void marcarFallida(String mensaje) {
        this.estado = "FALLIDA";
        this.mensaje = mensaje;
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

    public String getMensaje() {
        return mensaje;
    }
}