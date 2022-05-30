package com.dvomu.designpatterns.singleton;

/**
 * @author: dvomu
 * @create: 2022-05-30
 */
public class Singleton05 {
    private Singleton05() {
    }

    private static class SingletonHolder{
        private static  Singleton05 HOLDER = new Singleton05();
    }

    public static Singleton05 getInstance() {
        return SingletonHolder.HOLDER;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                System.out.println(Singleton05.getInstance().hashCode());
            }).start();
        }
    }
}
