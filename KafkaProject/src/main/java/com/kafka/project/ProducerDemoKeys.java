package com.kafka.project;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ProducerDemoKeys{

        final static Logger logger = LoggerFactory.getLogger(ProducerDemoKeys.class);

        public static void main(String[] args) throws ExecutionException, InterruptedException {
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

                String topic = "first_topic";
                String value = "hello word" + Integer.toString(i);
                String key = "id " + Integer.toString(i);
                //create Producer record
                ProducerRecord<String, String> record = new ProducerRecord<>(topic,key, value);

                logger.info("Key: " + key);

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
                }).get(); //block to send to make synchronous - don't do this in production

            }

            producer.flush();

            producer.close();
        }
}
