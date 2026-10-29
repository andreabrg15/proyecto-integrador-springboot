package com.axity.curso.retailservice.dto;

import java.util.List;

public record OrderDTO(
    Long customerId,
    List<OrderItemDTO> items
) {}
