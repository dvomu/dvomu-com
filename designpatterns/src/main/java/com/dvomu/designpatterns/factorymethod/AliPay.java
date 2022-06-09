package com.dvomu.designpatterns.factorymethod;

/**
 * @author: dvomu
 * @create: 2022-06-01
 */
public class WeChartPay implements PayChannel{
    @Override
    public void pay() {
        System.out.println("微信支付...");
    }
}
