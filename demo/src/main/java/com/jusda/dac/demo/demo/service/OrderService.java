package com.jusda.dac.demo.demo.service;


import com.jusda.dac.demo.demo.module.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    Page<Order> findAll(Pageable pageable);
}
