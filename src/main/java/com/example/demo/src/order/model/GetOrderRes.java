package com.example.demo.src.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetOrderRes {

    private int orderId;
    private int orderDetailId;
    private String status;
    private String productName;
    private int price;
    private String payment;
    private int quantity;
    private String productOption;

}
