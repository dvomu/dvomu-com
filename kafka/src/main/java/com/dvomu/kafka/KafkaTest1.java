package com.dvomu.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * bin/kafka-topics.sh --bootstrap-server q102:9092 --topic kafka-test-topic01 --create --partitions 2 --replication-factor 2
 */
public class KafkaTest1 {
    String topic = "kafka-test-topic01";
    @Test
    public void testProducer() throws ExecutionException, InterruptedException {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "q101:9092,q102:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        while (true){
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    String key = "item" + j;
                    String value = "value" + i;
                    Future<RecordMetadata> send = producer.send(new ProducerRecord<>(topic, key, value));
                    RecordMetadata rm = send.get();
                    int partition = rm.partition();
                    long offset = rm.offset();
                    System.out.println("key:" + key + " value:" + value + " partition:" + partition  + " offset:" + offset);
                }
            }
        }
    }

    @Test
    public void testConsumer() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"q101:9092,q102:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG,"test-group01");

        /**
         * earliest:当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费
         * latest 当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，消费新产生的该分区下的数据
         * none:没有找到提交的offset 报错
         * 注意:提交过offset，latest和earliest没有区别，但是在没有提交offset情况下，用latest会导致无法读取旧数据。
         */
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"none");
        //默认自动提交true
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,true);
        //自动提交时间,默认5s.自动提交容易造成丢数据和重复数据
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,5000);
        //每批次拉取数量量
//        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,"");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // kafka Consumer会自动负载
        consumer.subscribe(Arrays.asList(topic), new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                System.out.println("onPartitionsRevoked进入");
                Iterator<TopicPartition> iterator = partitions.iterator();
                while (iterator.hasNext()){
                    System.out.println("onPartitionsRevoked:"+iterator.next().partition());
                }
            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                System.out.println("onPartitionsAssigned进入");
                Iterator<TopicPartition> iterator = partitions.iterator();
                while (iterator.hasNext()){
                    System.out.println("onPartitionsAssigned:"+iterator.next().partition());
                }
            }
        });

        while (true){
            //每次拉取回来数据量0~多条
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(0));
            if(records.isEmpty()){
                continue;
            }
            Set<TopicPartition> partitions = records.partitions();
            //comsumer每批次可以拉取多个分区数据,
            partitions.stream().forEach(o->{
                String partition = String.valueOf(o.partition());
                //拉取多个分区后可以在此处使用多线程并行处理
                Iterable<ConsumerRecord<String, String>> re = records.records(partition);
                // 按分区消费
                System.out.println("=========按分区消费=======");
                while (re.iterator().hasNext()){
                    ConsumerRecord<String, String> record = re.iterator().next();
                    int p = record.partition();
                    long offset = record.offset();
                    String key = record.key();
                    String value = record.value();
                    System.out.println(" key:" + key + " value:" + value + " partition:" + p  + " offset:" + offset);
                }
            });

//            Iterator<ConsumerRecord<String, String>> iterator = records.iterator();
//            if(records.isEmpty()){
//                continue;
//            }
//            System.out.println("本次拉取消息数量:"+records.count());
//            while (iterator.hasNext()){
//                //当Consumer消费多个分区次,此处获取的msg可能是多个分区的数据
//                ConsumerRecord<String, String> record = iterator.next();
//                int partition = record.partition();
//                long offset = record.offset();
//                String key = record.key();
//                String value = record.value();
//                System.out.println("key:" + key + " value:" + value + " partition:" + partition  + " offset:" + offset);
//            }
        }

    }
}
