package com.axity.curso.retailservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.axity.curso.retailservice.model.Customer;

public interface CustomerRepository
    extends JpaRepository<Customer, Long> {

}
