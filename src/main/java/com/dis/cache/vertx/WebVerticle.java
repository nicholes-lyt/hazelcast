package com.dis.cache.vertx;

import com.dis.cache.util.BusAddress;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Scope("prototype")
public class WebVerticle extends AbstractVerticle {

    private final static String JSON_TYPE = "application/json;charset=UTF-8";

    @Value("${spring.vertx.server.port}")
    private int port;

    private int idleTimeout = 65;


    public Router router() {
        return Router.router(vertx);
    }

    public void start() {
        Router router = router();
        HttpServer httpServer = vertx.createHttpServer(new HttpServerOptions()
                .setCompressionSupported(true)
                .setTcpKeepAlive(true)
                .setTcpQuickAck(true)
                .setTcpFastOpen(true)
                .setTcpNoDelay(true)
                .setIdleTimeout(idleTimeout));

        // 异常处理
        router.route().failureHandler(f -> {
            if (f.failed()) {
                Throwable e = f.failure();
                if (e instanceof IllegalStateException && f.response().closed()) {
                    log.info("连接关闭", e);
                    return;
                } else {
                    log.error("发生错误", e);
                }
            }
            f.response().end();
        });

        router.get("/").handler(r -> {
            writeJSON(r, Buffer.buffer("vertx HttpServer success"));
        });

        router.route("/send").handler(r -> {
            vertx.eventBus().send(BusAddress.msgAddress, "send test msg", sr -> {
                log.info("send msg is:{}", sr.succeeded());
                if (sr.succeeded()) {
                    writeJSON(r, Buffer.buffer("send eventBus msg sucess"));
                } else {
                    writeJSON(r, Buffer.buffer("send eventBus msg sucess"));
                }
            });

            vertx.eventBus().publish(BusAddress.topicAddress, "send test Topic msg");

        });



        httpServer.requestHandler(router::accept).listen(port);
        log.info("vertx httpServer start");
    }

    private static void writeJSON(RoutingContext rc, Buffer buf) {
        rc.response()
                .putHeader(HttpHeaders.CONTENT_TYPE, JSON_TYPE)
                .putHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(buf.length()))
                .write(buf).setStatusCode(200).end();
    }

}
