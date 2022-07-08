package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private int id;
    private String pw;
    private String name;
    private String phone;
    private String email;
    private String profileImgUrl;
    private String wow;
    private int isPushNotice;
    private String status;
}
