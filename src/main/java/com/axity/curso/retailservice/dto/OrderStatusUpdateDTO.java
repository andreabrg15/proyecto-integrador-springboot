package com.axity.curso.retailservice.dto;

import com.axity.curso.retailservice.enumeration.OrderStatus;

public record OrderStatusUpdateDTO(
    OrderStatus status
) {}
