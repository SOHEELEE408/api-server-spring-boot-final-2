package com.example.demo.utils;

import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;


@Service
public class CookieService {

    /*
    Cookie 생성
    @param jwt
    @return Cookie
     */
    public Cookie createCookie(String jwt){
        Cookie cookie = new Cookie("jwt", jwt);
        cookie.setDomain("localhost");
        cookie.setPath("/");
        cookie.setMaxAge(60*60*60*24); // 24시간 유지

        return cookie;

    }


    /*
    Cookie 만료
    @param jwt
     */
    public void expireCookie(String jwt) {
        Cookie cookie= new Cookie("jwt",null);
        cookie.setMaxAge(0);
    }


}
