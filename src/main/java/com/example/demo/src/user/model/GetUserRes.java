package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserRes {
    private int id;
    private String name;
    private String phone;
    private String email;
    private String profileImgUrl;
    private String wow;
    private int isPushNotice;
}
