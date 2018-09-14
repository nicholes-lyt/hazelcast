package com.dis.cache.config;

import io.vertx.core.VertxOptions;
import io.vertx.spi.cluster.zookeeper.ZookeeperClusterManager;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VertxOptionsConfig {

    @ConfigurationProperties(prefix = "spring.zk")
    @Bean
    ZookeeperClusterManager zookeeperClusterManager(){
        return new ZookeeperClusterManager();
    }

    @ConfigurationProperties(prefix = "spring.vertx.options")
    @Bean
    VertxOptions vertxOptions(){new VertxOptions();
        return new VertxOptions().setClusterManager(zookeeperClusterManager());
    }



}
