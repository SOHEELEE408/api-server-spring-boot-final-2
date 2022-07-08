package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostUserReq {
    private String pw;
    private String pwCheck;
    private String name;
    private String phone;
    private String email;
    private int isPushNotice;
}
