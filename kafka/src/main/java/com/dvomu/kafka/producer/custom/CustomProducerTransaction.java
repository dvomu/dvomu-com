package com.dvomu.kafka.producer.custom;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * @author: dvomu
 * @create: 2022-04-27
 */
public class CustomProducerTransaction {
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

        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,"com.dvomu.kafka.producer.custom.MyPartitioner");

        //0.指定全局唯一事务ID
        properties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG,"tran_order_01");

        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        //1.初始化事务
        producer.initTransactions();
        //2.开启事务
        producer.beginTransaction();
        try {
            producer.send(new ProducerRecord<>("topicA","Hello"));
            //3.提交事务
            producer.commitTransaction();
        }catch (Exception e){
            //4.回滚事务
            producer.abortTransaction();
        }finally {
            //关闭资源
            producer.close();
        }
        System.out.println("发送完成");
    }
}
