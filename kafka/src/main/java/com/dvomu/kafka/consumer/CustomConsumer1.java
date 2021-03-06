package com.dvomu.kafka.consumer;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

/**
 * @author: dvomu
 * @create: 2022-05-14
 */
public class CustomConsumer1 {
    public static void main(String[] args) {
        // 0 配置
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"q101:9092,q102:9092");
        //反序列化
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());

        // 配置消费者组ID
        properties.put(ConsumerConfig.GROUP_ID_CONFIG,"test_consumer");

        //配置分区策略-RoundRobin
        properties.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG,
                "org.apache.kafka.clients.consumer.RoundRobinAssignor");

        //配置分区策略-StickyAssignor
//        properties.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG,
//                "org.apache.kafka.clients.consumer.StickyAssignor");


        // 1 创建消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // 2 定义主题 "topic-test-03"
        Collection<String> topics = new ArrayList<>();
        topics.add("topic-test-03");
        consumer.subscribe(topics);

        // 3 消费数据
        while (true){
            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, String> cr:consumerRecords) {
                System.out.println(cr);
            }
        }
    }
}
