package com.axity.curso.retailservice.service;

import com.axity.curso.retailservice.dto.OrderItemRequestDTO;
import com.axity.curso.retailservice.dto.OrderRequestDTO;
import com.axity.curso.retailservice.model.Order;
import com.axity.curso.retailservice.model.OrderItem;
import com.axity.curso.retailservice.enumeration.OrderStatus;
import com.axity.curso.retailservice.exception.RecursoNoEncontradoException;
import com.axity.curso.retailservice.exception.StockInsuficienteException;
import com.axity.curso.retailservice.model.Product;
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
    private final ProductRepository productRepository;

    public OrderService(
            OrderRepository orderRepository,
            ProductRepository productRepository) {

        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    // Despues de cada transaccion que afecte los registros, se guarda con save
    @Transactional
    public Order createOrder(OrderRequestDTO request) {

        // Validar la solicitud
        if (request == null) {
            throw new IllegalArgumentException(
                    "Formato invalido en request");
        }

        if (request.items() == null || request.items().isEmpty()) {
            throw new IllegalArgumentException(
                    "El pedido debe contener al menos un producto");
        }

        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PROCESSING);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        // Procesar cada producto solicitado
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
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No existe el producto solicitado"));

            if (!Boolean.TRUE.equals(product.getIsActive())) {
                throw new RecursoNoEncontradoException(
                        "El producto no está disponible");
            }

            int quantity = itemRequest.quantity();

            if (product.getStock() == null
                    || product.getStock() < quantity) {
                throw new StockInsuficienteException(
                        "Stock insuficiente para: "
                                + product.getName());
            }

            BigDecimal unitPrice = product.getPrice();

            BigDecimal subtotal = unitPrice.multiply(
                    BigDecimal.valueOf(quantity));

            // Crear el detalle del pedido
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setUnitPrice(unitPrice);

            orderItems.add(orderItem);
            total = total.add(subtotal);

            // Descontar el inventario
            product.setStock(product.getStock() - quantity);
        }

        order.setItems(orderItems);
        order.setTotal(total);

        return orderRepository.save(order);
    }

    // Transaccion para consulta
    @Transactional(readOnly = true)
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
        .orElseThrow(() ->
                new RecursoNoEncontradoException(
                        "Pedido no encontrado con ID:" + id
                )
        );
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order updateOrderStatus(Long id, OrderStatus newStatus) {

        if (newStatus == null) {
        throw new IllegalArgumentException(
                "El estado del pedido es obligatorio");
        }

        Order order = orderRepository.findById(id)
        .orElseThrow(() -> new RecursoNoEncontradoException(
                "No existe el pedido solicitado"));

        OrderStatus currentStatus = order.getStatus();

        boolean validTransition =
        (currentStatus == OrderStatus.PROCESSING
                && (newStatus == OrderStatus.SHIPPED
                || newStatus == OrderStatus.CANCELLED));

        if (!validTransition) {
        throw new IllegalArgumentException(
                "No se permite cambiar el pedido de "
                        + currentStatus + " a " + newStatus);
        }

        order.setStatus(newStatus);

        return orderRepository.save(order);
    }
    
    @Transactional
    public Order cancelOrder(Long id) {
        
        Order order = orderRepository.findById(id)
        .orElseThrow(() -> new RecursoNoEncontradoException(
        "No existe el pedido solicitado"));
        
        // Valida que pueda cancelarse
        if (order.getStatus() != OrderStatus.PROCESSING) {
                throw new IllegalArgumentException(
                "Solo se pueden cancelar pedidos en proceso");
        }
        
        for (OrderItem item : order.getItems()) {
                
        Product product = item.getProduct();
        
        // Devolver las unidades al inventario
        product.setStock(
        product.getStock() + item.getQuantity()
        );

        }
        
        order.setStatus(OrderStatus.CANCELLED);
        
        return orderRepository.save(order);
    }
}
