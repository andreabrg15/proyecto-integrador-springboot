package com.axity.curso.retailservice.controller;

import com.axity.curso.retailservice.dto.OrderRequestDTO;
import com.axity.curso.retailservice.dto.OrderResponseDTO;
import com.axity.curso.retailservice.model.Order;
import com.axity.curso.retailservice.model.OrderItem;
import com.axity.curso.retailservice.service.OrderService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    private OrderResponseDTO.OrderItemResponseDTO toItemResponseDTO(
            OrderItem item) {

        return new OrderResponseDTO.OrderItemResponseDTO(
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getSubtotal()
        );
    }

    // Agregar pedido
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDTO createOrder(
            @RequestBody OrderRequestDTO request) {

        Order order = orderService.createOrder(request);

        List<OrderResponseDTO.OrderItemResponseDTO> items =
                order.getItems().stream()
                        .map(this::toItemResponseDTO)
                        .toList();

        return new OrderResponseDTO(
                order.getId(),
                order.getOrderDate(),
                order.getStatus(),
                order.getTotal(),
                items
        );
    }

    // Todos los pedidos
    @GetMapping
    public List<OrderResponseDTO> getAllOrders() {
        return orderService.getAllOrders()
            .stream()
            .map(order -> {
                List<OrderResponseDTO.OrderItemResponseDTO> items =
                    order.getItems().stream()
                    .map(this::toItemResponseDTO)
                    .toList();

                return new OrderResponseDTO(
                    order.getId(),
                    order.getOrderDate(),
                    order.getStatus(),
                    order.getTotal(),
                    items
                );
            })
            .toList();
    }
    

    // Pedido por id
    @GetMapping("/{id}")
    public OrderResponseDTO getOrderById(@PathVariable Long id) {

        Order order = orderService.getOrderById(id);

        List<OrderResponseDTO.OrderItemResponseDTO> items =
                order.getItems().stream()
                        .map(this::toItemResponseDTO)
                        .toList();

        return new OrderResponseDTO(
            order.getId(),
            order.getOrderDate(),
            order.getStatus(),
            order.getTotal(),
            items
        );
    }
    
}
