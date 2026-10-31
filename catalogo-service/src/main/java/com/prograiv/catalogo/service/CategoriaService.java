package com.prograiv.catalogo.service;

import com.prograiv.catalogo.dto.CategoriaResponse;
import com.prograiv.catalogo.entity.Categoria;
import com.prograiv.catalogo.repository.CategoriaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponse> listar() {
        List<Categoria> categorias = categoriaRepository.findAll(Sort.by("nombre"));
        List<CategoriaResponse> respuesta = new ArrayList<>();

        for (Categoria categoria : categorias) {
            respuesta.add(new CategoriaResponse(categoria.getId(), categoria.getNombre()));
        }

        return respuesta;
    }
}
