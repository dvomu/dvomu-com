package com.dvomu.designpatterns.prototype;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: dvomu
 * @create: 2022-06-07
 */
public class ShallowClone {
    public static void main(String[] args) {
        User u = new User();
        u.setValues("张三");

        User cloneU2 = (User) u.clone();
        cloneU2.setValues("李四");
        // 输出结果:[张三, 李四]
        System.out.println(u.getValues());
    }
}

class User implements Cloneable{
    private List<String> values = new ArrayList();

    @Override
    protected Object clone()  {
        User user = null;
        try {
            user = (User) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return user;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(String v) {
        this.values.add(v);
    }
}