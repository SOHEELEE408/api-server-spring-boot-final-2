package com.example.demo.src.user;

import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/app/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;



    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * 회원 조회 API
     * [GET] /users
     * 회원 번호 및 이메일 검색 조회 API
     * [GET] /users? Email=
     * @return BaseResponse<List<GetUserRes>>
     */


    /**
     * 회원 1명 조회 API
     * [GET] /users/:userIdx
     * @return BaseResponse<GetUserRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{userId}") // (GET) 127.0.0.1:9000/app/users/:userIdx
    public BaseResponse<GetUserRes> getUser(@PathVariable("userId") int userId) {
        // Get Users
        try{
            GetUserRes getUserRes = userProvider.getUser(userId);
            return new BaseResponse<>(getUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 회원가입 API
     * [POST] /users
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        // TODO: email 관련한 짧은 validation 예시입니다. 그 외 더 부가적으로 추가해주세요!
        if(postUserReq.getEmail() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL); // 이메일을 입력해주세요.
        }

        if(postUserReq.getName() == null){
           return new BaseResponse<>(POST_USERS_EMPTY_NAME); // 이름을 입력해주세요.
        }

        if(postUserReq.getPw() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD); // 비밀번호를 입력해주세요.
        }

        if(!postUserReq.getPwCheck().equals(postUserReq.getPw())){
            return new BaseResponse<>(POST_USERS_INCONSISTENCY_PASSWORD); // 비밀번호와 비밀번호 확인의 입력내용일 다를 때
        }

        if(postUserReq.getPhone() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_PHONE); // 연락처를 입력해주세요.
        }

        //이메일 정규표현
        if(!isRegexEmail(postUserReq.getEmail())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL); // 이메일 형식을 확인해주세요.
        }
        try{
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 로그인 API
     * [POST] /users/logIn
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/logIn")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq){

        if(postLoginReq.getEmail() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL); // 이메일을 입력해주세요.
        }

        if(postLoginReq.getPw() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD); // 비밀번호를 입력해주세요.
        }

        if(!isRegexEmail(postLoginReq.getEmail())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL); // 이메일 형식을 확인해주세요.
        }

        try{
            // TODO: 로그인 값들에 대한 형식적인 validatin 처리해주셔야합니다!
            // TODO: 유저의 status ex) 비활성화된 유저, 탈퇴한 유저 등을 관리해주고 있다면 해당 부분에 대한 validation 처리도 해주셔야합니다.
            PostLoginRes postLoginRes = userProvider.logIn(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 유저정보변경 API
     * [PATCH] /users/:userIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userId}/name")
    public BaseResponse<String> modifyUserName(@PathVariable("userId") int userId, @RequestBody PatchUserReq patchUserReq){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 유저네임 변경
            userService.modifyUserName(patchUserReq);

            String result = "이름이 변경되었습니다.";
        return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PatchMapping("/{userId}/email")
    public BaseResponse<String> modifyUserEmail(@PathVariable("userId") int userId, @RequestBody PatchUserReq patchUserReq){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 유저 이메일 변경
            userService.modifyUserEmail(patchUserReq);

            String result = "이메일이 변경되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PatchMapping("/{userId}/phone")
    public BaseResponse<String> modifyUserPhone(@PathVariable("userId") int userId, @RequestBody PatchUserReq patchUserReq){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 유저 연락처 변경
            userService.modifyUserPhone(patchUserReq);

            String result = "연락처가 변경되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PatchMapping("/{userId}/password")
    public BaseResponse<String> modifyUserPassword(@PathVariable("userId") int userId, @RequestBody PatchPasswordReq patchPasswordReq){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            //같다면 유저네임 변경
            userService.modifyUserPassword(patchPasswordReq);

            String result = "비밀번호가 성공적으로 변경되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }

    @ResponseBody
    @PatchMapping("/{userId}/push")
    public BaseResponse<String> modifyUserPush(@PathVariable("userId") int userId, @RequestBody PatchUserReq patchUserReq){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 유저네임 변경
            userService.modifyUserPush(patchUserReq);

            String result = "알림 상태가 변경되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PatchMapping("/{userId}/deleted")
    public BaseResponse<String> deleteUser(@PathVariable("userId") int userId, @RequestBody PatchPasswordReq patchPasswordReq){
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            userService.deleteUser(patchPasswordReq);
            String result = "회원 탈퇴 처리가 완료되었습니다.";

            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


}
