package com.example.demo.src.cart.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    private int cartId;
    private int userId;
    private int productId;
    private int quantity;
    private int optionId;

}
