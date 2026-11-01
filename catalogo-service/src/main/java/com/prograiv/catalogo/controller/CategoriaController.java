package com.prograiv.catalogo.controller;

import com.prograiv.catalogo.dto.CategoriaResponse;
import com.prograiv.catalogo.service.CategoriaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")

@Tag(
        name = "Categorias",
        description = "Categorias disponibles para los productos"
)

public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }


    @Operation(summary = "Consultar las categorias disponibles")

    @GetMapping
    public List<CategoriaResponse> listar() {
        return categoriaService.listar();
    }
}
