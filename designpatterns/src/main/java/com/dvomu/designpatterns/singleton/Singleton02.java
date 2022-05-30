package com.dvomu.designpatterns.singleton;

import org.junit.Test;

/**
 * @author: dvomu
 * @create: 2022-05-30
 */
public class Singleton02 {
    private static Singleton02 INSTANCE = null;

    private Singleton02() {
    }

    public static Singleton02 getInstance() {
        if (INSTANCE == null) { //此处存在线程不安全
            INSTANCE = new Singleton02();
        }
        return INSTANCE;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(()->{
                System.out.println(Singleton02.getInstance().hashCode());
            }).start();
        }
    }
}
