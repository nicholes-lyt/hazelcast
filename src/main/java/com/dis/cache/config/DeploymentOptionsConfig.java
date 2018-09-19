package com.dis.cache.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.vertx.core.DeploymentOptions;


@Configuration
public class DeploymentOptionsConfig {

    @Bean
    DeploymentOptions deploymentOptions(){
        return new DeploymentOptions();
    }

}
