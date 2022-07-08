package com.example.demo.src.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostOrderReq {
    private int productId;
    private int userId;
    private int addressId;
    private int optionId;
    private int quantity;
    private String productName;
    private int totalProductsPrice;
    private int discount;
    private int deliveryFee;
    private int coupangCash;
    private int totalPaymentPrice;
    private String payment;
    private String company;
    private String paymentNumber;
    private int installments;
    private int isDafaultPayment;
    private String cashReceiptNumber;
    private String status;

}
