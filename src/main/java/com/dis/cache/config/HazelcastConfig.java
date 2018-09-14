package com.dis.cache.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.ManagementCenterConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfig {

    @Value("${spring.hazelcast-mancenter.url}")
    private String hazelcastMancenterUrl;

    @Value("${spring.hazelcast-mancenter.enable}")
    private boolean managementEnbale;

    @Bean
    Config config(){
        Config config = new Config();
        ManagementCenterConfig mcc = new ManagementCenterConfig().setUrl(hazelcastMancenterUrl).setEnabled(managementEnbale);
        config.setManagementCenterConfig(mcc);
        return config;
    }

}
