package com.jusda.dac.demo.demo;

import com.jusda.dac.demo.demo.constants.OrderSource;
import com.jusda.dac.demo.demo.module.Order;
import com.jusda.dac.demo.demo.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Date;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    OrderRepository orderRepository;

    @Test
    public void test(){
        Order order = new Order();
        order.setId("2");
        order.setCreatedAt(new Date());
        order.setCreatedBy("李四");
        order.setDescription("测试数据2");
        order.setOrderSource(OrderSource.STORE);
        order.setTotalAmount(100);

        orderRepository.save(order);

        orderRepository.getOne("1");
    }

}
