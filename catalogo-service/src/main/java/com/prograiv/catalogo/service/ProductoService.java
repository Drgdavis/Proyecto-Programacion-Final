package com.prograiv.catalogo.service;

import com.prograiv.catalogo.dto.PaginaResponse;
import com.prograiv.catalogo.dto.ProductoResponse;
import com.prograiv.catalogo.entity.Producto;
import com.prograiv.catalogo.repository.ProductoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public PaginaResponse<ProductoResponse> listar(String codigo, String nombre,
                                                  Long categoriaId, int page, int size) {
        if (page < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La pagina debe ser igual o mayor que cero.");
        }
        if (size < 1 || size > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El tamanio de pagina debe estar entre 1 y 100.");
        }
        if (categoriaId != null && categoriaId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El identificador de categoria debe ser mayor que cero.");
        }

        PageRequest paginacion = PageRequest.of(page, size, Sort.by("id"));
        Page<Producto> productos = productoRepository.buscar(
                normalizarFiltro(codigo), normalizarFiltro(nombre), categoriaId, paginacion);
        List<ProductoResponse> contenido = new ArrayList<>();

        for (Producto producto : productos.getContent()) {
            contenido.add(convertir(producto));
        }

        return new PaginaResponse<>(contenido, productos.getNumber(), productos.getSize(),
                productos.getTotalElements(), productos.getTotalPages());
    }

    public ProductoResponse obtenerPorId(Long id) {
        if (id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El identificador de producto debe ser mayor que cero.");
        }

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No existe un producto con el identificador solicitado."));

        return convertir(producto);
    }

    private String normalizarFiltro(String filtro) {
        if (filtro == null || filtro.isBlank()) {
            return null;
        }
        return filtro.trim();
    }

    private ProductoResponse convertir(Producto producto) {
        return new ProductoResponse(producto.getId(), producto.getCodigo(), producto.getNombre(),
                producto.getCategoria().getId(), producto.getCategoria().getNombre(),
                producto.getPrecio(), producto.getExistencia());
    }
}
