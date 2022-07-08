package com.example.demo.src.socialLogin;


import com.example.demo.config.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final GoogleOauth googleOauth;
    private final HttpServletResponse response;

    public void request(SocialLoginType socialLoginType) throws BaseException {
        String redirectURL = null;
        if(socialLoginType.equals(SocialLoginType.GOOGLE)){
            redirectURL = googleOauth.getOauthRedirectURL();
        }

        try{
            response.sendRedirect(redirectURL);
        }catch(IOException exception){
            throw new BaseException(SOCIALLOGIN_ERROR);
        }
    }

    public String requestAccessToken(SocialLoginType socialLoginType, String code) {
        return googleOauth.requestAccessToken(code);
    }
}
