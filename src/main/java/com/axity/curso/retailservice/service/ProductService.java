package com.axity.curso.retailservice.service;

import com.axity.curso.retailservice.model.Product;
import com.axity.curso.retailservice.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

// Capa de Negocio
@Service
public class ProductService {

    private final ProductRepository productRepository;

    // Spring proporciona automaticamente una instancia del repositorio
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Despues de cada transaccion que afecte los registros, se guarda con save
    @Transactional
    public Product createProduct(Product product) {

        validateProduct(product, false);

        product.setIsActive(true);

        return productRepository.save(product);
    }

    // Transaccion para consulta
    @Transactional(readOnly = true)
    public Product getProductById(Long id) {

        return productRepository.findByIdAndIsActiveTrue(id)
            .orElseThrow(() ->
                    new RuntimeException(
                            "Producto no encontrado o inactivo con ID: " + id
                    )
            );
    }

    // Transaccion para consulta con opcion de filtros
    @Transactional(readOnly = true)
    public List<Product> getProducts(
        String category,
        BigDecimal minPrice,
        BigDecimal maxPrice 
    ) 
    {
        if (category != null && minPrice != null && maxPrice != null) {
            return productRepository
            .findByCategoryIgnoreCaseAndPriceBetweenAndIsActiveTrue(category, minPrice, maxPrice);
        }

        if (category != null && minPrice != null) {
            return productRepository
            .findByCategoryIgnoreCaseAndPriceGreaterThanEqualAndIsActiveTrue(category, minPrice);
        }

        if (category != null && maxPrice != null) {
            return productRepository
            .findByCategoryIgnoreCaseAndPriceLessThanEqualAndIsActiveTrue(category, maxPrice);
        }

        if (category != null) {
            return productRepository.findByCategoryIgnoreCaseAndIsActiveTrue(category);
        }

        if (minPrice != null && maxPrice != null) {
            return productRepository.findByPriceBetweenAndIsActiveTrue(minPrice, maxPrice);
        }

        if (minPrice != null) {
            return productRepository.findByPriceGreaterThanEqualAndIsActiveTrue(minPrice);
        }

        if (maxPrice != null) {
            return productRepository.findByPriceLessThanEqualAndIsActiveTrue(maxPrice);
        }

        return productRepository.findByIsActiveTrue();
    }

    @Transactional
    public Product updateProduct(Long id, Product updatedProduct) {

        Product existingProduct = getProductById(id);

        validateProduct(updatedProduct, true);

        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setStock(updatedProduct.getStock());

        return productRepository.save(existingProduct);
    }

    // BORRADO LOGICO
    @Transactional
    public void deactivateProduct(Long id) {

        Product product = getProductById(id);

        product.setIsActive(false);

        productRepository.save(product);
    }

    private void validateProduct(Product product, boolean isUpdate) {

        if (!isUpdate) {
            if (product.getName() == null ||
                    product.getName().isBlank()) {
                throw new IllegalArgumentException(
                        "El nombre del producto es obligatorio"
                );
            }

            if (product.getCategory() == null ||
                    product.getCategory().isBlank()) {
                throw new IllegalArgumentException(
                        "La categoría es obligatoria"
                );
            }
        }

        if (product.getPrice() == null ||
                product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "El precio no puede ser negativo ni nulo"
            );
        }

        if (product.getStock() == null || product.getStock() < 0) {
            throw new IllegalArgumentException(
                    "El stock no puede ser negativo ni nulo"
            );
        }
    }
}
