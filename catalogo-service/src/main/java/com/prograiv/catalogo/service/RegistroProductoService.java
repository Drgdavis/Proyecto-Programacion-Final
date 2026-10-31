package com.prograiv.catalogo.service;

import com.prograiv.catalogo.dto.ProductoLoteRequest;
import com.prograiv.catalogo.entity.Categoria;
import com.prograiv.catalogo.entity.Producto;
import com.prograiv.catalogo.repository.CategoriaRepository;
import com.prograiv.catalogo.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistroProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public RegistroProductoService(
            ProductoRepository productoRepository,
            CategoriaRepository categoriaRepository
    ) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long registrar(ProductoLoteRequest entrada) {

        String codigo = entrada.codigo().trim();
        String nombre = entrada.nombre().trim();
        String nombreCategoria = entrada.categoria().trim();

        if (productoRepository.existsByCodigoIgnoreCase(codigo)) {
            throw new IllegalArgumentException(
                    "El codigo del producto ya esta registrado."
            );
        }

        Categoria categoria = categoriaRepository
                .findByNombreIgnoreCase(nombreCategoria)
                .orElseThrow(() -> new IllegalArgumentException(
                        "La categoria indicada no existe."
                ));

        Producto producto = new Producto(
                codigo,
                nombre,
                categoria,
                entrada.precio(),
                entrada.existencia()
        );

        Producto guardado = productoRepository.saveAndFlush(producto);

        return guardado.getId();
    }
}