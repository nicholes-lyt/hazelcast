package com.dis.cache;

import com.dis.cache.hazelcast.HazelcastClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    @Autowired
    private static HazelcastClient hazelcastClient;

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
        //hazelcastClient.startClient();
    }

}
