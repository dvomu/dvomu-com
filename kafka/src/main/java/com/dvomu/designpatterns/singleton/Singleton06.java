package com.dvomu.designpatterns.singleton;

public enum Singleton06 {
    INSTANCE;

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                System.out.println(Singleton06.INSTANCE.hashCode());
            }).start();
        }
    }
}
