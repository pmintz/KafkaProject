package com.kafka.project;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoWithCallbank {

    final static Logger logger = LoggerFactory.getLogger(ProducerDemoWithCallbank.class);

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
        for(int i = 0; i <10; i++) {
            //create Producer record
            ProducerRecord<String, String> record = new ProducerRecord<>("first_topic", "hello world: " + i);

            //send data - asynchronous
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (e == null) {
                        logger.info("Received new metadata \n" +
                                "Topic: " + recordMetadata.topic() + " \n" +
                                "Partition: " + recordMetadata.partition() + " \n" +
                                "Offset: " + recordMetadata.offset() + " \n" +
                                "Timestamp: " + recordMetadata.timestamp());
                    } else {
                        logger.error("Error while producing: ", e);

                    }
                }
            });

        }

        producer.flush();

        producer.close();
    }
}
