package com.example.demo.src.cart.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetCartRes {
    private int cartId;
    private int productId;
    private int optionId;
    private String productName;
    private int quantity;
    private int totalPrice;
    private String deliveryType;
    private int deliveryFee;
    private int freeShippingStandard;

    public GetCartRes(int productId, int quantity, int deliveryFee, int optionId) {
        this.productId = productId;
        this.quantity = quantity;
        this.deliveryFee = deliveryFee;
        this.optionId = optionId;
    }
}
