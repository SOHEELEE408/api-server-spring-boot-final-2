package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
public class Product {
    private int productId;
    private int categoryId;
    private String productName;
    private int price;
    private int deliveryFee;
    private String deliveryType;
    private String caurierCompany;
    private String brand;
    private int stock;
    private String caution;
    private String comp;
    private int weight;
    private int discountRate;
    private int forcastDeliveryRate;

}
