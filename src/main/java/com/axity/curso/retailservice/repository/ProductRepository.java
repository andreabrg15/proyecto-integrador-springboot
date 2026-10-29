package com.axity.curso.retailservice.repository;

import com.axity.curso.retailservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository
        extends JpaRepository<Product, Long> {

    // Encontrar un producto por categoria ignorando minusculas/mayusculas
    List<Product> findByCategoryIgnoreCase(String category);

    // Encontrar un producto por rango de precio
    List<Product> findByPriceBetween(
        java.math.BigDecimal minPrice,
        java.math.BigDecimal maxPrice
    );

    // Encontrar productos activos
    List<Product> findByIsActiveTrue();

    // Encontrar productos activos por id
    Optional<Product> findByIdAndIsActiveTrue(Long id);
}
