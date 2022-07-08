package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EMPTY_NAME(false, 2017, "이름을 입력해주세요."),
    POST_USERS_EMPTY_PASSWORD(false, 2018, "비밀번호를 입력해주세요."),
    POST_USERS_EMPTY_PHONE(false, 2019, "연락처를 입력해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2020,"중복된 이메일입니다."),
    POST_USERS_INCONSISTENCY_PASSWORD(false, 2021, "비밀번호가 일치하지 않습니다. 동일한 비밀번호를 입력해주세요."),

    // [POST] /carts
    POST_CARTS_NO_USER(false, 2030, "로그인 후 이용해주세요."),

    // [POST] /address
    POST_ADDRESS_EMPTY_RECEIVEDNAME (false, 2040, "수취인의 이름을 입력해주세요."),
    POST_ADDRESS_EMPTY_POSTNUMBER (false, 2041, "우편번호를 입력해주세요."),
    POST_ADDRESS_EMPTY_ADDRESS (false, 2042, "주소를 입력해주세요."),
    POST_ADDRESS_EMPTY_PHONE (false, 2043, "수취인의 연락처를 입력해주세요."),

    // [PATCH] /orders
    PATCH_ORDER_INVALID_STATUS(false, 2044, "배송 또는 반품, 교환 처리 중인 상품은 취소할 수 없습니다."),

    // [POST] /reviews
    POST_REVIEW_EMPTY_SCORE(false, 2045, "평점을 입력해주세요."),
    POST_REVIEW_EMPTY_CONTENTS(false, 2046, "후기 내용을 입력해주세요."),


    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),
    DELETED_USER(false, 3015, "탈퇴한 회원입니다."),

    // [PATCH] /reviews
    DELETED_REVIEW(false, 3016, "삭제 처리된 리뷰입니다."),


    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),
    SOCIALLOGIN_ERROR(false, 4002, "소셜로그인 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),
    MODIFY_FAIL_USEREMAIL(false,4015,"유저 이메일 수정 실패"),
    MODIFY_FAIL_USERPASSWORD(false,4016,"유저 비밀번호 수정 실패"),
    MODIFY_FAIL_USERPHONE(false,4017,"유저 연락처 수정 실패"),
    MODIFY_FAIL_USERPUSH(false,4018,"유저 푸시 알림 수정 실패"),
    MODIFY_FAIL_DEFAULTPAYMENT(false, 4019, "기본 결제 수단 설정 실패"),

    PASSWORD_ERROR(false, 4019, "비밀번호를 확인해주세요."),
    USER_DELETE_FAIL(false,4020,"회원탈퇴 처리 실패"),

    MODIFY_FAIL_CART(false, 4030, "장바구니 업데이트 실패"),
    DELETE_FAIL_CART(false, 4031, "장바구니 업데이트 실패"),

    ADD_ADDRESS_FAIL(false, 4040, "배송지 추가 실패"),
    MODIFY_ADDRESS_FAIL(false, 4041, "배송지 수정 실패"),
    DELETE_ADDRESS_FAIL(false, 4042, "배송지 삭제 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),

    PATCH_FAIL_CANCEL_ORDER(false, 4013, "주문 취소를 실패했습니다."),
    MODIFY_FAIL_STATUS_ORDER(false, 4014, "교환/반품 신청이 실패했습니다"),

    MODIFY_FAIL_REVIEW(false, 4015, "후기 수정 실패"),
    DELETE_FAIL_REVIEW(false, 4016, "후기 삭제 처리 실패");
    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
