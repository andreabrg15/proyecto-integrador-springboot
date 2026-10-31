package com.axity.curso.retailservice.dto;

import java.math.BigDecimal;

public record ProductUpdateDTO(
    
    String description,
    BigDecimal price,
    Integer stock
) {}