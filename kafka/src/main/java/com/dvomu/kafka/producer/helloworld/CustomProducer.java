package com.dvomu.kafka.producer.helloworld;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @author: dvomu
 * @create: 2022-04-27
 */
public class CustomProducer {
    public static void main(String[] args) {
        Properties properties = new Properties();
        //连接集群
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "q101:9092,q102:9092");
        //指定对应的key和value虚拟化类型
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName());

        //1.创建kafka生产者
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        //2.发送数据
        for (int i = 0; i < 5; i++) {
//            异步发送
//            producer.send(new ProducerRecord<>("topicA", "hello kafka" + i));
//            待回调的异步发送
//            producer.send(new ProducerRecord<>("topicA",
//                    "hello kafka" + i),
//                    new Callback() {
//                @Override
//                public void onCompletion(RecordMetadata metadata, Exception e) {
//                    if(e==null){
//                        System.out.println("主题:"+metadata.topic()+",分区:"+metadata.partition());
//                    }
//                }
//            });
//            同步发送
            try {
                producer.send(new ProducerRecord<>("topicA", "hello kafka" + i)).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        System.out.println("发送完成");
        //3.关闭资源
        producer.close();
    }
}
