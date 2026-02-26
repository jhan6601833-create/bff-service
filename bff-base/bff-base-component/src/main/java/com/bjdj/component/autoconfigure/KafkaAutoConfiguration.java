package com.bjdj.component.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.LoggingProducerListener;
import org.springframework.kafka.support.ProducerListener;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(KafkaTemplate.class)
@ConditionalOnProperty(prefix = "bff.component.kafka", name = "enabled", havingValue = "true", matchIfMissing = true)
public class KafkaAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ProducerListener<Object, Object> producerListener() {
        return new LoggingProducerListener<>();
    }
}
