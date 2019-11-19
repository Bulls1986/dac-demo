package com.jusda.dac.demo.demo.repository;

import com.jusda.dac.demo.demo.module.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,String> {
}
