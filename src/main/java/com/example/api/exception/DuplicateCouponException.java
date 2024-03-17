package com.example.api.exception;

public class DuplicateCouponException extends RuntimeException {
    public DuplicateCouponException() {
        super("중복 쿠폰 발급 오류");
    }
}
