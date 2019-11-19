package com.jusda.dac.demo.demo.rest;


import com.jusda.dac.demo.demo.module.Order;
import com.jusda.dac.demo.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public Page<Order> findAll(Pageable pageable){
        return orderService.findAll(pageable);
    }
}
