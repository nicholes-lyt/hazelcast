package com.dis.cache;

import com.dis.cache.service.RabbitMQService;
import io.vertx.core.json.JsonObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRabbiMQ {

    @Autowired
    private RabbitMQService rabbitMQService;

    @Test
    public void send(){
        JsonObject json = new JsonObject();
        json.put("body","hello msg send-2");
        rabbitMQService.send(json);
        get();
    }

    @Test
    public void get(){
        rabbitMQService.get("queue-1");
    }
}
