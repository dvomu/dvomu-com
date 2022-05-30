package com.dvomu.designpatterns.singleton;


/**
 * @author: dvomu
 * @create: 2022-05-30
 */
public class Singleton01 {

    private static final Singleton01 singleton = new Singleton01();

    private Singleton01(){}

    public static Singleton01 getInstance(){
        return singleton;
    }
}
