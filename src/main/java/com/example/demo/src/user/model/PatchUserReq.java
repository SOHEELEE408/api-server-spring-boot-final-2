package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatchUserReq {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String pw;
    private int isPushNotice;
    private String defaultPayment;
}
