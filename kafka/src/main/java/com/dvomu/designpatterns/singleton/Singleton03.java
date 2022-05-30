package com.dvomu.designpatterns.singleton;

import org.junit.Test;

/**
 * @author: dvomu
 * @create: 2022-05-30
 */
public class Singleton03 {
    private static Singleton03 SINGLETON= null;
    private Singleton03(){}

    public static synchronized Singleton03 getInstance(){
        if(SINGLETON==null){
            SINGLETON = new Singleton03();
        }
        return SINGLETON;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(()->{
                System.out.println(Singleton03.getInstance().hashCode());
            }).start();
        }
    }
}
