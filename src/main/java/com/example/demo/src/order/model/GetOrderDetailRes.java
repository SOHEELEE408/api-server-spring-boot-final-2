package com.example.demo.src.order.model;

import com.example.demo.src.address.model.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetOrderDetailRes {

    private List<GetOrderRes> orderDetails;
    private Address address;
    private String payment;
    private String company;
    private String paymentNumber;
    private int totalProductsPrice;
    private int deliveryFee;
    private int discount;
    private int totalPaymentPrice;

}
