package com.example.api.exception;

public class CouponIssuanceFailureException extends RuntimeException {
    public CouponIssuanceFailureException() {super("쿠폰 발급 오류");}
}
