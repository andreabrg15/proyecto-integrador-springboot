package com.axity.curso.retailservice.dto;

public record OrderItemRequestDTO(
    Long productId,
    Integer quantity
) {}
