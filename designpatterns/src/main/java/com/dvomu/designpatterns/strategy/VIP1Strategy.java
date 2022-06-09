package com.dvomu.designpatterns.strategy;

/**
 * @author: dvomu
 * @create: 2022-05-31
 */
public class VIP0Strategy implements VIPStrategy{
    @Override
    public Integer payMoney(Integer payMoney) {
        return payMoney;
    }
}
