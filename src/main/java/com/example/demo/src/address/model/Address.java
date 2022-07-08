package com.example.demo.src.address.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private int addressId;
    private int userId;
    private String receivedName;
    private String postNumber;
    private String address;
    private String detailAddress;
    private String phone;
    private String request;
    private int defaultAddress;
}
