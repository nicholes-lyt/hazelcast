package com.dis.cache.hazelcast;

import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Queue;

@Slf4j
@Component
public class HazelcastClient{

    private static String mapName = "clasterMap";

    private static String queueName = "queue";

    public static void startClient(){
        ClientConfig config = new ClientConfig();
        HazelcastInstance instance = com.hazelcast.client.HazelcastClient.newHazelcastClient(config);
        Map<Integer,String> clusterMap = instance.getMap(mapName);
        Queue<String> queue = instance.getQueue(queueName);

        log.info("client get Map value:"+clusterMap.get(1));
        log.info("client get Queue size:"+queue.size());
        log.info("client get Queue value:"+queue.poll());
    }

}