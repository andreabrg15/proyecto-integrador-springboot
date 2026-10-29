package com.axity.curso.retailservice.dto;

import com.axity.curso.retailservice.enumeration.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(
        Long id,
        LocalDateTime orderDate,
        OrderStatus status,
        BigDecimal total,
        List<OrderItemResponseDTO> items
) {
    public record OrderItemResponseDTO(
            Long productId,
            String productName,
            Integer quantity,
            BigDecimal unitPrice,
            BigDecimal subtotal
    ) {}
}

