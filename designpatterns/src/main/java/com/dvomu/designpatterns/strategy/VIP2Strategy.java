package com.dvomu.designpatterns.strategy;

/**
 * @author: dvomu
 * @create: 2022-05-31
 */
public class VIP1Strategy implements VIPStrategy {
    @Override
    public Integer payMoney(Integer payMoney) {
        return (int)Math.floor(payMoney * 0.9);
    }
}
