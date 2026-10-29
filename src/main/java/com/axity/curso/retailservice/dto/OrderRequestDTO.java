package com.axity.curso.retailservice.dto;

import java.util.List;

public record OrderRequestDTO(
    Long customerId,
    List<OrderItemRequestDTO> items
) {}
