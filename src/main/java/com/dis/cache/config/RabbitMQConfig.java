package com.dis.cache.config;

import io.vertx.rabbitmq.RabbitMQOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RabbitMQConfig {

    @ConfigurationProperties(prefix = "spring.rabbitmq")
    @Bean
    RabbitMQOptions rabbitMQOptions(){
        return new RabbitMQOptions();
    }

}
