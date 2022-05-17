package com.dvomu.kafka.producer.custom;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * @author: dvomu
 * @create: 2022-04-27
 */
public class CustomProducerForConsumerTest {
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
        String msg = "pay123";
        //2.发送数据
        for (int i = 0; i < 5000; i++) {
            producer.send(new ProducerRecord<>("topic-test-03",
                            msg+i),
                    new Callback() {
                        @Override
                        public void onCompletion(RecordMetadata metadata, Exception e) {
                            if (e == null) {
                                System.out.println("主题:" + metadata.topic() + ",分区:" + metadata.partition());
                            }
                        }
                    });
        }

        System.out.println("发送完成");
        //3.关闭资源
        producer.close();
    }
}
