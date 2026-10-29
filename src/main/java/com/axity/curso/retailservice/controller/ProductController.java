package com.axity.curso.retailservice.controller;

import com.axity.curso.retailservice.dto.ProductResponseDTO;
import com.axity.curso.retailservice.model.Product;
import com.axity.curso.retailservice.service.ProductService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    private ProductResponseDTO toProductResponseDTO(Product product) {

        return new ProductResponseDTO(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getCategory(),
            product.getPrice(),
            product.getStock()
        );
    }

    @GetMapping("/{id}")
    public ProductResponseDTO getProduct(@PathVariable Long id) {
        return toProductResponseDTO(productService.getProductById(id));
    }
    
}
