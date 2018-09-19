package com.dis.cache.vertx;

import com.dis.cache.service.RabbitMQService;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Slf4j
@Component
public class VertxCluster {

    @Autowired
    private WebVerticle webVerticle;

    @Autowired
    private ReceiveVerticle receiveVerticle;

    @Resource
    private VertxOptions vertxOptions;

    @Autowired
    private RabbitMQService rabbitMQService;

    @Resource
    private DeploymentOptions deploymentOptions;

    private Vertx vertx;

    @PostConstruct
    public void init() {
        // 启动集群和引擎
        Vertx.clusteredVertx(vertxOptions, res -> {
            if (res.succeeded()) {
                vertx = res.result();
                log.info("Vertx实例已加入集群,\n nodeId:{}" +
                                "\n current:{}" +
                                "\n clusterPort:{}" +
                                "\n clusterPublicHost:{}" +
                                "\n clusterPublicPort:{}",
                        vertxOptions.getClusterManager().getNodeID(),
                        vertxOptions.getClusterHost(),
                        vertxOptions.getClusterPort(),
                        vertxOptions.getClusterPublicHost(),
                        vertxOptions.getClusterPublicPort());

                vertx.deployVerticle(webVerticle, r -> {
                    if (r.succeeded()) {
                        log.info("vertx httpServer start complete");
                    } else {
                        log.warn("vertx httpServer start faile");
                    }
                });

                vertx.deployVerticle(receiveVerticle, r -> {
                    if (r.succeeded()) {
                        log.info("receiveVerticle start complete");
                    } else {
                        log.warn("receiveVerticle start faile");
                    }
                });

                vertx.deployVerticle(rabbitMQService,new DeploymentOptions(deploymentOptions));

            } else {
                log.error("Vertx集群启动失败", res.cause());
                System.exit(0);
            }
        });
    }


}
