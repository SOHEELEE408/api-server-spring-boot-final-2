package com.example.demo.src.cart.model;


import com.example.demo.src.order.model.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostCartOrderReq {

    private List<Integer> cartIds;
    private int userId;
    private int addressId;
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


    public PostCartOrderReq(int userId, int addressId, int totalProductsPrice, int discount, int deliveryFee, int coupangCash, int totalPaymentPrice, String payment, String company, String paymentNumber, int installments, int isDafaultPayment, String cashReceiptNumber, String status) {
        this.userId = userId;
        this.addressId = addressId;
        this.totalProductsPrice = totalProductsPrice;
        this.discount = discount;
        this.deliveryFee = deliveryFee;
        this.coupangCash = coupangCash;
        this.totalPaymentPrice = totalPaymentPrice;
        this.payment = payment;
        this.company = company;
        this.paymentNumber = paymentNumber;
        this.installments = installments;
        this.isDafaultPayment = isDafaultPayment;
        this.cashReceiptNumber = cashReceiptNumber;
        this.status = status;
    }
}
