package com.prograiv.catalogo.controller;

import com.prograiv.catalogo.dto.LoteProductosRequest;
import com.prograiv.catalogo.dto.LoteProductosResponse;
import com.prograiv.catalogo.dto.PaginaResponse;
import com.prograiv.catalogo.dto.ProductoResponse;
import com.prograiv.catalogo.service.LoteProductosService;
import com.prograiv.catalogo.service.ProductoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/productos")
@Tag(
        name = "Productos",
        description = "Consultas y registro de productos"
)

public class ProductoController {

    private final ProductoService productoService;
    private final LoteProductosService loteProductosService;

    public ProductoController(
            ProductoService productoService,
            LoteProductosService loteProductosService
    ) {
        this.productoService = productoService;
        this.loteProductosService = loteProductosService;
    }

    @Operation(
            summary = "Buscar productos con paginacion",
            description = "Filtros opcionales: codigo, nombre y categoriaId. "
                    + "page comienza en 0; size admite valores entre 1 y 100."
    )

    @GetMapping
    public PaginaResponse<ProductoResponse> listar(
            @RequestParam(name = "codigo", required = false) String codigo,
            @RequestParam(name = "nombre", required = false) String nombre,
            @RequestParam(name = "categoriaId", required = false) Long categoriaId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return productoService.listar(
                codigo, nombre, categoriaId, page, size
        );
    }

    @Operation(summary = "Consultar un producto por identificador")

    @GetMapping("/{id}")
    public ProductoResponse obtenerPorId(@PathVariable("id") Long id) {
        return productoService.obtenerPorId(id);
    }

    @Operation(
            summary = "Registrar productos por lote",
            description = "Evalua cada producto y devuelve su itemId, "
                    + "aceptacion, productoId o motivo de rechazo. "
                    + "Los productos validos se conservan aunque otros se rechacen."
    )

    @PostMapping("/lote")
    public LoteProductosResponse registrarLote(
            @RequestBody LoteProductosRequest solicitud
    ) {
        return loteProductosService.procesar(solicitud);
    }
}