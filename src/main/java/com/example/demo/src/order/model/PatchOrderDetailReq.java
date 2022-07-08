package com.example.demo.src.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatchOrderDetailReq {
    private int orderId;
    private int orderDetailId;
    private String status;
    private String newStatus;
    private int price;
    private int deliveryFee;
    private int quantity;
}
