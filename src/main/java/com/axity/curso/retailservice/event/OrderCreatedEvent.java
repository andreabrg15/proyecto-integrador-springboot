package com.axity.curso.retailservice.event;

import java.math.BigDecimal;

public record OrderCreatedEvent(
        Long orderId,
        BigDecimal total
) {
}