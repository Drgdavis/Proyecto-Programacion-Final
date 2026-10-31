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

@RestController
@RequestMapping("/api/productos")
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

    @GetMapping("/{id}")
    public ProductoResponse obtenerPorId(@PathVariable("id") Long id) {
        return productoService.obtenerPorId(id);
    }

    @PostMapping("/lote")
    public LoteProductosResponse registrarLote(
            @RequestBody LoteProductosRequest solicitud
    ) {
        return loteProductosService.procesar(solicitud);
    }
}