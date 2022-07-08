package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetProductRes {
    private int productId;
    private int categoryId;
    private String productName;
    private int price;
    private String deliveryType;
    private String comp;
    private int weight;
    private int discountRate;
    private int forcastDeliveryRate;
}
