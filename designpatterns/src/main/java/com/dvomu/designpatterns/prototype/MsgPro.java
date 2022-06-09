package com.dvomu.designpatterns.prototype;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * @author: dvomu
 * @create: 2022-06-07
 */
@Data
@ToString
public class Msg {
    //收件人
    private String receiver;
    //消息名称
    private String subject;
    private String context;

    public Msg(Template template){
        this.subject = template.getName();
        this.context = template.getContext();
    }
    public Msg(String receiver,Template template) {
        this(template);
        this.receiver = receiver;
    }
}
