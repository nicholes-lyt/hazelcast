package com.dis.cache.vertx;

import com.dis.cache.util.BusAddress;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReceiveVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> future) throws Exception {
        vertx.eventBus().localConsumer(BusAddress.msgAddress, this::receive);
        vertx.eventBus().consumer(BusAddress.topicAddress, this::receive);
        future.complete();
    }

    private void receive(Message<String> msg) {
        log.info("eventBus receive msg is:{}", msg.body());
        msg.reply(msg.body());
    }

}
