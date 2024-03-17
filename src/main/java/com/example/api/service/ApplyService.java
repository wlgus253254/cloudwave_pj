package com.example.api.service;

import com.example.api.exception.*;
import com.example.api.producer.CouponCreateProducer;
import com.example.api.repository.AppliedUserRepository;
import com.example.api.repository.CouponCountRepository;
import com.example.api.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Service;

@Service
public class ApplyService {

    private final CouponRepository couponRepository;

    private final CouponCountRepository couponCountRepository;

    private final CouponCreateProducer couponCreateProducer;

    private final AppliedUserRepository appliedUserRepository;

    @Value("${coupon.max-count}")
    private Long maxCount;

    public ApplyService(
            CouponRepository couponRepository,
            CouponCountRepository couponCountRepository,
            CouponCreateProducer couponCreateProducer,
            AppliedUserRepository appliedUserRepository) {
        this.couponRepository = couponRepository;
        this.couponCountRepository = couponCountRepository;
        this.couponCreateProducer = couponCreateProducer;
        this.appliedUserRepository = appliedUserRepository;
    }

    public void apply(Long userId) {
        Long apply = appliedUserRepository.add(userId);
        System.out.println("여기까지는 redis에 추가가 됨");

        // 쿠폰 중복 발급 오류
        if (apply != 1) {
            System.out.println("error in duplicated userID");
            throw new DuplicateCouponException();
        }

        Long count = couponCountRepository.increment();
        System.out.println("여기까지는 redis에 count++고 count는 -> " + count);

        // 쿠폰 한정 수량 초과 오류
        if (count > this.maxCount) {
            Long delete = appliedUserRepository.remove(userId);
            System.out.println("coupon issuance limit exceeded");
            throw new CouponLimitExceededException();
        }

        System.out.println("여기까지는 redis 잘됨");

        couponCreateProducer.create(userId);
        System.out.println("coupon created");
    }

//    public void apply2(Long userId) {
//        Long apply = appliedUserRepository.add(userId);
//        synchronized (this) {
//            if (coupon_count == 0) {
//                Long delete = appliedUserRepository.remove(userId);
//                System.out.println("coupon issuance limit exceeded");
//                throw new CouponLimitExceededException();
//            }
//            coupon_count--;
//        }
//
//        System.out.println("초과 발급 오류 없이 userId를 redis에 추가함");
//
//        // 쿠폰 중복 발급 오류
//        if (apply != 1) {
//            System.out.println("error in duplicated userID");
//            throw new DuplicateCouponException();
//        }
//
//        System.out.println("중복 발급 오류 없이 userId를 redis에 추가함");
//        System.out.println("count는 -> " + coupon_count);
//
//        couponCreateProducer.create(userId);
//        System.out.println("coupon created");
//    }

}
