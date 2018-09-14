package com.dis.cache.hazelcast;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Queue;


@Slf4j
@Component
public class HazelcastServer {

    private static String mapName = "clasterMap";

    private static String queueName = "queue";

    @PostConstruct
    public void init(){
        start();
        get();
        //get2();
    }

    public void start(){
        HazelcastInstance instance = Hazelcast.newHazelcastInstance();
        //创建map集群
        Map<Integer,String> clusterMap = instance.getMap(mapName);
        clusterMap.put(1,"hello hazelcast map");

        //创建queue
        Queue<String> queue = instance.getQueue(queueName);
        queue.offer("hello hazelcast queue");

    }

    public void get(){
        HazelcastInstance instance = Hazelcast.newHazelcastInstance();
        Map<Integer,String> clusterMap = instance.getMap(mapName);
        Queue<String> queue = instance.getQueue(queueName);

        log.info("Map value: "+clusterMap.get(1));
        log.info("Queue size: "+queue.size());
        log.info("Queue value: "+queue.poll());


    }

    public void get2(){
        HazelcastInstance instance2 = Hazelcast.newHazelcastInstance();
        Map<Integer,String> clusterMap2 = instance2.getMap(mapName);
        Queue<String> queue2 = instance2.getQueue(queueName);

        log.info("2 Map value: "+clusterMap2.get(1));
        log.info("2 Queue size: "+queue2.size());
        log.info("2 Queue value: "+queue2.poll());
    }
}
