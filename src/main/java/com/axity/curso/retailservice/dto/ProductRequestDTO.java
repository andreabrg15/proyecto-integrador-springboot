package com.axity.curso.retailservice.dto;

import java.math.BigDecimal;

public record ProductRequestDTO(
    
    String name,
    String description,
    String category,
    BigDecimal price,
    Integer stock
) {}
