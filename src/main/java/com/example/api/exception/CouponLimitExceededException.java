package com.example.api.exception;

public class CouponLimitExceededException extends RuntimeException {
    public CouponLimitExceededException() {
        super("쿠폰 초과 발급 오류");
    }
}
