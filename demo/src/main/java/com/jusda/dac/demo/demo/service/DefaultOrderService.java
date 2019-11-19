package com.jusda.dac.demo.demo.service;

import com.jusda.dac.demo.demo.module.Order;
import com.jusda.dac.demo.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DefaultOrderService implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }
}
