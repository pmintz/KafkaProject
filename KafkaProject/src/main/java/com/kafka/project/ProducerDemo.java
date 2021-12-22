package com.kafka.project;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class ProducerDemo {
    public static void main(String[] args) {
        System.out.println("hello world");

        //create Producer properties

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "192.168.1.12:9092");
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());

        //properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.12:9092");

        //create Producer
        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);

        //create Producer record
        ProducerRecord<String, String> record = new ProducerRecord<>("first_topic", "hello world");

        //send data - asynchronous
        producer.send(record);

        producer.flush();

        producer.close();
    }
}
