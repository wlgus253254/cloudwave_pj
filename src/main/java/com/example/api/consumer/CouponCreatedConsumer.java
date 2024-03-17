package com.example.api.consumer;

import com.example.api.domain.Coupon;
import com.example.api.domain.FailedEvent;
import com.example.api.repository.CouponRepository;
import com.example.api.repository.FailedEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CouponCreatedConsumer {

    private final CouponRepository couponRepository;

    private final FailedEventRepository failedEventRepository;

    private final Logger logger = LoggerFactory.getLogger(CouponCreatedConsumer.class);

    public CouponCreatedConsumer(CouponRepository couponRepository, FailedEventRepository failedEventRepository) {
        this.couponRepository = couponRepository;
        this.failedEventRepository = failedEventRepository;
    }

    @KafkaListener(topics = "example", groupId = "group_1")
    public void listener(Long userId) {
        try {
            System.out.println("Received message for userId: " + userId);
            couponRepository.save(new Coupon(userId));
            System.out.println("우하하하");
        } catch (Exception e) {
            logger.error("failed to create coupon::" + userId);
            failedEventRepository.save(new FailedEvent(userId));
        }
    }
}
