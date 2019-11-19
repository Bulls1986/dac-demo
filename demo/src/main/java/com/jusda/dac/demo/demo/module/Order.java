package com.jusda.dac.demo.demo.module;

import com.jusda.dac.demo.demo.constants.OrderSource;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_order")
@Data
public class Order {

    /**
     * id
     */
    @Id
    private String id;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 订单来源
     */
    @Enumerated(EnumType.STRING)
    private OrderSource orderSource;

    /**
     * 订单总额
     */
    private double totalAmount;

    /**
     * 订单描述
     */
    private String description;
}
