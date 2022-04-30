package com.dvomu.kafka.producer.custom;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * @author: dvomu
 * @create: 2022-04-27
 */
public class CustomProducerParam {
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

        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,
                                "com.dvomu.kafka.producer.custom.MyPartitioner");
        /**
         * 自定义消息发送参数
         */
        //bathch.size:批次大小,默认16K.这里修改成32K
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG,32*1024);

        //linger.ms:等待时间,默认0.这里修改成5
        properties.put(ProducerConfig.LINGER_MS_CONFIG,5);

        //RecordAccumulator:缓冲区大小,默认32M.这里保持不变
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG,32*1024*1024);

        //compression.type:压缩,默认none,可配置:gzip\snappy\lz4\zstd
        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG,"snappy");
        //1.创建kafka生产者
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        //2.发送数据
        for (int i = 0; i < 100; i++) {
            producer.send(new ProducerRecord<>("topicA",
                            "msg"+1),
                    new Callback() {
                        @Override
                        public void onCompletion(RecordMetadata metadata, Exception e) {
                            if(e==null){
                                System.out.println("主题:"+metadata.topic()+",分区:"+metadata.partition());
                            }
                        }
                    });
        }

        System.out.println("发送完成");
        //3.关闭资源
        producer.close();
    }
}
