package com.example.demo.src.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {

    private int orderDetailId;
    private int orderId;
    private int productId;
    private int quantity;
    private int deliveryFee;
    private int optionId;
    private String status;
}
