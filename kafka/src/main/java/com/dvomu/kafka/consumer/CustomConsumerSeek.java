package com.dvomu.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.Set;

/**
 * @author: dvomu
 * @create: 2022-05-14
 */
public class CustomConsumerSeek {
    public static void main(String[] args) {
        // 0 配置
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "q101:9092,q102:9092");
        //反序列化
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());

        // 配置消费者组ID
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test_consumer");

        // 1 创建消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        //指定位置消费
        Set<TopicPartition> assignment = consumer.assignment();

        // 2 定义主题 "test_consumer_01"
        Collection<String> topics = new ArrayList<>();
        topics.add("test_consumer_01");
        consumer.subscribe(topics);

        //保证分区分配方案已经指定完毕
        while (assignment.size() == 0) {
            consumer.poll(Duration.ofSeconds(1));
            assignment = consumer.assignment();
        }

        // 指定消费offset
        for (TopicPartition partition : assignment) {
            consumer.seek(partition, 600);
        }

        // 3 消费数据
        while (true) {
            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, String> cr : consumerRecords) {
                System.out.println(cr);
            }
        }
    }
}
