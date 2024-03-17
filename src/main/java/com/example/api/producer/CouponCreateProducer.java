package com.example.api.producer;

import com.example.api.exception.CouponIssuanceFailureException;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class CouponCreateProducer {

    private final KafkaTemplate<String, Long> kafkaTemplate;

    public CouponCreateProducer(KafkaTemplate<String, Long> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void create(Long userId) {
        try {
            kafkaTemplate.send("example", userId);
            System.out.println("send completed");
        } catch (KafkaProducerException e) {
            System.out.println("Kafka 프로듀서에서 예외가 발생했습니다");
            throw new CouponIssuanceFailureException();
        }
    }
}
