package com.dis.cache;

import io.vertx.core.Vertx;

/**
 * vertx创建定时操作
 */
public class TestPeriodic {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
//        vertx.setPeriodic(10, i ->{
//            System.out.println("periodic run------"+System.currentTimeMillis());
//        });

        long timerId = vertx.setTimer(1,i -> {
            System.out.println("vertx timer run------"+System.currentTimeMillis());
        });

        System.out.println("timerId:"+timerId);
        vertx.cancelTimer(timerId);
    }

}
