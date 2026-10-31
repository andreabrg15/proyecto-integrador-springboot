package com.axity.curso.retailservice.event;

public record StockChangedEvent(
        Long productId,
        String productName,
        Integer previousStock,
        Integer newStock
) {
}