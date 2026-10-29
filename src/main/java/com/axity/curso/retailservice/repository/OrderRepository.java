package com.axity.curso.retailservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.axity.curso.retailservice.model.Order;

public interface OrderRepository
    extends JpaRepository<Order, Long> {

}
