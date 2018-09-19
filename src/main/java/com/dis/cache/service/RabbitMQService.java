package com.dis.cache.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.rabbitmq.RabbitMQClient;
import io.vertx.rabbitmq.RabbitMQOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitMQService extends AbstractVerticle {

    @Value("${spring.rabbitmq.queue}")
    private String queue;

    @Autowired
    private RabbitMQOptions rabbitMQOptions;

    private RabbitMQClient rabbitMQClient;

    private String mqAddress = "mq-queue";

    @Override
    public void start(Future<Void> future) throws Exception {
        rabbitMQClient = RabbitMQClient.create(vertx, rabbitMQOptions);
        rabbitMQClient.start(res -> {
            log.info("mq uri:{}", rabbitMQOptions.getUri());
            if (res.succeeded()) {
                log.info("rabbitMQClient start successed");

                // 消息总线获取MQ中数据
                get();
                rabbitMQClient.basicConsume(queue, mqAddress, r -> {
                    if (r.succeeded()) {
                        log.info("consume MQ success queue:{}",queue);
                    } else {
                        log.error("consume MQ error:{}",r.cause());
                    }
                });

                future.complete();
            } else {
                log.error("rabbitMQ start error:{}", res.cause());
                future.fail(res.cause());
            }
        });
    }

    public void send(JsonObject json) {
        rabbitMQClient.basicPublish("", queue, json, r -> {
            if (r.succeeded()) {
                log.info("rabbitMQ published");
            } else {
                log.error("rabbitMQ send error {}", r.cause());
            }
        });
        vertx.eventBus().localConsumer(queue, this::onReceive);
    }


    public void get(String queue) {
        rabbitMQClient.basicGet(queue, false, r -> {
            if (r.succeeded()) {
                log.info("receive MQ msg:{}", r.result());
            } else {
                log.error("receive MQ error:{}", r.cause());
            }
        });
    }

    public void get() {
        log.info("eventBus consume MQ:{}", queue);
        vertx.eventBus().localConsumer(mqAddress, this::onReceive);
    }

    private void onReceive(Message<Object> msg) {
        log.info("eventBus receive MQ msg:{}", msg.body());
    }

}
