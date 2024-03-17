package com.example.api.config;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import java.util.*;

@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.properties.topic}")
    private String topic_name;

    @Value("${aws.accessKeyId}")
    private String awsAccessKey;

    @Value("${aws.secretKey}")
    private String awsSecretKey;

    @Value("${aws.username}")
    private String awsUsername;

    @Value("${kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;

    // 토픽을 생성하는 메소드
    @Bean
    public NewTopic topicName() {
        return TopicBuilder.name(topic_name) // 토픽 이름을 지정
                .partitions(6) // 파티션 개수를 지정 (옵션)
                .build();
    }

    // KafkaAdmin을 구성하는 메소드
    @Bean
    public KafkaAdmin kafkaAdmin() {
        return new KafkaAdmin(kafkaConfig()); // KafkaAdmin을 생성하고 Kafka 구성을 전달
    }

    // Kafka 구성을 정의하는 메소드
    // 실제로는 해당 메소드를 구현하여 Kafka 구성을 반환하는 로직을 추가해야 합니다.
    private Map<String, Object> kafkaConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        config.put("aws.accessKeyId", awsAccessKey);
        config.put("aws.secretKey", awsSecretKey);

        config.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
        config.put(SaslConfigs.SASL_MECHANISM, "AWS_MSK_IAM");
        config.put(SaslConfigs.SASL_JAAS_CONFIG, "software.amazon.msk.auth.iam.IAMLoginModule required awsProfileName=\"" + awsUsername + "\";");
        config.put(SaslConfigs.SASL_CLIENT_CALLBACK_HANDLER_CLASS, "software.amazon.msk.auth.iam.IAMClientCallbackHandler");

        return config;
    }
}
