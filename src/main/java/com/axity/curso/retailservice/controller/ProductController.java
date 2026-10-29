package com.axity.curso.retailservice.controller;

import com.axity.curso.retailservice.dto.ProductRequestDTO;
import com.axity.curso.retailservice.dto.ProductResponseDTO;
import com.axity.curso.retailservice.model.Product;
import com.axity.curso.retailservice.service.ProductService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


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

    // Todos los productos
    @GetMapping
    public List<ProductResponseDTO> getAllProducts() {
        return productService.getAllProducts()
                .stream()
                .map(this::toProductResponseDTO)
                .toList();
    }

    // Producto por Id (SKU)
    @GetMapping("/{id}")
    public ProductResponseDTO getProduct(@PathVariable Long id) {
        return toProductResponseDTO(productService.getProductById(id));
    }

    // Todos los productos por categoria
    @GetMapping("/category/{category}")
    public List<ProductResponseDTO> getProductsByCategory(
            @PathVariable String category) {

        return productService.getProductsByCategory(category)
                .stream()
                .map(this::toProductResponseDTO)
                .toList();
    }

    // Agregar Producto
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDTO createProduct(
            @RequestBody ProductRequestDTO request) {

        Product product = new Product();

        product.setName(request.name());
        product.setDescription(request.description());
        product.setCategory(request.category());
        product.setPrice(request.price());
        product.setStock(request.stock());

        Product createdProduct = productService.createProduct(product);

        return toProductResponseDTO(createdProduct);
    }

    // Modificar Producto
    @PutMapping("/{id}")
    public ProductResponseDTO updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequestDTO request) {

        Product updatedProduct = new Product();

        updatedProduct.setName(request.name());
        updatedProduct.setDescription(request.description());
        updatedProduct.setCategory(request.category());
        updatedProduct.setPrice(request.price());
        updatedProduct.setStock(request.stock());

        Product product = productService.updateProduct(id, updatedProduct);

        return toProductResponseDTO(product);
    }

    // Borrado Logico de Producto
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateProduct(@PathVariable Long id) {
        productService.deactivateProduct(id);
    }
    
}
