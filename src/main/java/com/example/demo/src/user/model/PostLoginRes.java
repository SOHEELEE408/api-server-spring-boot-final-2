package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.Cookie;

@Getter
@Setter
@AllArgsConstructor
public class PostLoginRes {

    private int id;
    private String jwt;
}
