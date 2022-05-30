package com.dvomu.designpatterns.singleton;

import org.junit.Test;

/**
 * @author: dvomu
 * @create: 2022-05-30
 */
public class Singleton04 {
    private static volatile Singleton04 SINGLETON = null;

    private Singleton04() {
    }

    public static Singleton04 getInstance() {
        if (SINGLETON == null) {
            synchronized (Singleton04.class) {
                if (SINGLETON == null) {
                    SINGLETON = new Singleton04();
                }
            }

        }
        return SINGLETON;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                System.out.println(Singleton04.getInstance().hashCode());
            }).start();
        }
    }
}
