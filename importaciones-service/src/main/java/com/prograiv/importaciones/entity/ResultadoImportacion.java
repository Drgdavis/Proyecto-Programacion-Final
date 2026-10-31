package com.prograiv.importaciones.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "resultados_importacion")
public class ResultadoImportacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "importacion_id", nullable = false)
    private Importacion importacion;

    @Column(nullable = false)
    private int fila;

    @Column(length = 255)
    private String codigo;

    @Column(nullable = false)
    private boolean aceptado;

    @Column(name = "producto_id")
    private Long productoId;

    @Column(columnDefinition = "TEXT")
    private String motivo;

    protected ResultadoImportacion() {
    }

    public ResultadoImportacion(
            Importacion importacion,
            int fila,
            String codigo,
            boolean aceptado,
            Long productoId,
            String motivo) {

        this.importacion = importacion;
        this.fila = fila;
        this.codigo = codigo;
        this.aceptado = aceptado;
        this.productoId = productoId;
        this.motivo = motivo;
    }

    public Long getId() {
        return id;
    }

    public int getFila() {
        return fila;
    }

    public String getCodigo() {
        return codigo;
    }

    public boolean isAceptado() {
        return aceptado;
    }

    public Long getProductoId() {
        return productoId;
    }

    public String getMotivo() {
        return motivo;
    }
}