package com.axity.curso.retailservice.repository;

import com.axity.curso.retailservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

public interface ProductRepository
        extends JpaRepository<Product, Long> {

    // Encontrar un producto por categoria ignorando minusculas/mayusculas
    List<Product> findByCategoryIgnoreCaseAndIsActiveTrue(String category);

    // Encontrar productos de este o mayor precio
    List<Product> findByPriceGreaterThanEqualAndIsActiveTrue(BigDecimal minPrice);

    // Encontrar productos de este o menor precio
    List<Product> findByPriceLessThanEqualAndIsActiveTrue(BigDecimal maxPrice);

    // Encontrar productos de este rango de precio
    List<Product> findByPriceBetweenAndIsActiveTrue(BigDecimal minprice, BigDecimal maxPrice);

    // Encontrar un producto por categoria y de este o mayor precio
    List<Product> findByCategoryIgnoreCaseAndPriceGreaterThanEqualAndIsActiveTrue(
        String category,
        BigDecimal minPrice
    );

    // Encontrar un producto por categoria y de este o menor precio
    List<Product> findByCategoryIgnoreCaseAndPriceLessThanEqualAndIsActiveTrue(
        String category,
        BigDecimal maxPrice
    );

    // Encontrar productos de esta categoria y rango de precio
    List<Product> findByCategoryIgnoreCaseAndPriceBetweenAndIsActiveTrue(
        String category,
        BigDecimal minPrice,
        BigDecimal maxPrice
    );

    // Encontrar productos activos
    List<Product> findByIsActiveTrue();

    // Encontrar productos activos por id
    Optional<Product> findByIdAndIsActiveTrue(Long id);
}
