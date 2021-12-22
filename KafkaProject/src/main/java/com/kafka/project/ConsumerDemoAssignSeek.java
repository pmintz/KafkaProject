package com.kafka.project;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

public class ConsumerDemoAssignSeek {

    final static Logger logger = LoggerFactory.getLogger(ConsumerDemo.class.getName());
    static String bootstrapServers = "192.168.1.12:9092";
    static String groupId = "my-fifth-application";
    static String topic = "first_topic";
    public static void main(String[] args) {
        System.out.println("Hellow world");
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        //properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        //create Consumer
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(properties);
        //kafkaConsumer.subscribe(Arrays.asList(("First_Topic", "Second_Tupic"));
        //kafkaConsumer.subscribe(Collections.singleton(topic));

        //assign and seek
        TopicPartition partitionToReadFrom = new TopicPartition(topic, 0);
        long offsetToReadFrom = 15l;
        kafkaConsumer.assign(Arrays.asList(partitionToReadFrom));

        //seek
        kafkaConsumer.seek(partitionToReadFrom, offsetToReadFrom);

        int numberOfMessages = 5;
        boolean keepOnReading = true;
        int numberOfMessagesReadSoFar = 0;

        while(keepOnReading){
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(1000));

            for(ConsumerRecord<String,String> record : records){
                numberOfMessagesReadSoFar +=1;
                logger.info("Key: " + record.key() + " Value: " + record.value());
                logger.info("Partition: " + record.partition() + " Offset: " + record.offset());
                if(numberOfMessagesReadSoFar >= numberOfMessages){
                    keepOnReading = false;
                    break;
                }
            }
        }

        logger.info("Exiting the application");


        //poll for data
       /* while(true){
            ConsumerRecords<String,String> records = kafkaConsumer.poll(Duration.ofMillis(1000));
            for(ConsumerRecord<String, String> record: records){
                logger.info("Key: " + record.key() + " value: " + record.value());
                logger.info("Partition: " + record.partition() + " Offset: " + record.offset());
            }
        }*/

    }
}
