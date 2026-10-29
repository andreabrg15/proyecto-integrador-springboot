package com.axity.curso.retailservice.service;

import com.axity.curso.retailservice.dto.OrderItemRequestDTO;
import com.axity.curso.retailservice.dto.OrderRequestDTO;
import com.axity.curso.retailservice.model.Customer;
import com.axity.curso.retailservice.model.Order;
import com.axity.curso.retailservice.model.OrderItem;
import com.axity.curso.retailservice.enumeration.OrderStatus;
import com.axity.curso.retailservice.model.Product;
import com.axity.curso.retailservice.repository.CustomerRepository;
import com.axity.curso.retailservice.repository.OrderRepository;
import com.axity.curso.retailservice.repository.ProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public OrderService(
            OrderRepository orderRepository,
            CustomerRepository customerRepository,
            ProductRepository productRepository) {

        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Order createOrder(OrderRequestDTO request) {

        // 1. Validar la solicitud
        if (request == null || request.customerId() == null) {
            throw new IllegalArgumentException(
                    "El ID del cliente es obligatorio");
        }

        if (request.items() == null || request.items().isEmpty()) {
            throw new IllegalArgumentException(
                    "El pedido debe contener al menos un producto");
        }

        // 2. Buscar al cliente
        Customer customer = customerRepository
                .findById(request.customerId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe el cliente solicitado"));

        // 3. Preparar el pedido
        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        // 4. Procesar cada producto solicitado
        for (OrderItemRequestDTO itemRequest : request.items()) {

            if (itemRequest == null
                    || itemRequest.productId() == null) {
                throw new IllegalArgumentException(
                        "El ID del producto es obligatorio");
            }

            if (itemRequest.quantity() == null
                    || itemRequest.quantity() <= 0) {
                throw new IllegalArgumentException(
                        "La cantidad debe ser mayor que cero");
            }

            Product product = productRepository
                    .findById(itemRequest.productId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "No existe el producto solicitado"));

            if (!Boolean.TRUE.equals(product.getIsActive())) {
                throw new IllegalArgumentException(
                        "El producto no está disponible");
            }

            int quantity = itemRequest.quantity();

            if (product.getStock() == null
                    || product.getStock() < quantity) {
                throw new IllegalArgumentException(
                        "Stock insuficiente para: "
                                + product.getName());
            }

            // 5. Calcular el importe de la línea
            BigDecimal unitPrice = product.getPrice();

            BigDecimal subtotal = unitPrice.multiply(
                    BigDecimal.valueOf(quantity));

            // 6. Crear el detalle del pedido
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setUnitPrice(unitPrice);

            orderItems.add(orderItem);
            total = total.add(subtotal);

            // 7. Descontar el inventario
            product.setStock(product.getStock() - quantity);
        }

        // 8. Asociar los detalles y el total
        order.setItems(orderItems);
        order.setTotal(total);

        // 9. Guardar el pedido y sus detalles
        return orderRepository.save(order);
    }
}
