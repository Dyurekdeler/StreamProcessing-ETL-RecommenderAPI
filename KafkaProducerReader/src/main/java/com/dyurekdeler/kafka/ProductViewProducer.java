package com.dyurekdeler.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.stream.Stream;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ProductViewProducer {

    public ProductViewProducer(){}

    public static void main(String[] args) throws IOException {
        new ProductViewProducer().run();
    }

    public void run() throws IOException {
        Logger logger = LoggerFactory.getLogger(ProductViewProducer.class);

        //create a kafka producer
        KafkaProducer<String, String> producer = createKafkaProducer();

        //loop to send data to kafka
        try (Stream<String> stream = Files.lines(Paths.get("src/main/resources/product-views.json"))) {
            stream.forEachOrdered(viewEvent -> {

                Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                //publish one event in a second
                wait(1000);

                JsonObject jsonObject = JsonParser.parseString(viewEvent).getAsJsonObject();

                jsonObject.addProperty("click_timestamp", timestamp.toString());
                viewEvent = new Gson().toJson(jsonObject);

                producer.send(new ProducerRecord<>("topic_clickevent", null, viewEvent), new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                        if(e!=null){
                            //failure
                            logger.error("Error occured! ", e);
                        }
                        else{
                            logger.info(
                                    "Topic: " + recordMetadata.topic() + "\n" +
                                    "Partition: " + recordMetadata.partition() + "\n" +
                                    "Offset: " + recordMetadata.offset() + "\n" +
                                    "Timestamp: " + recordMetadata.timestamp() + "\n");
                        }
                    }
                });
            });
        }
    }

    public KafkaProducer<String, String> createKafkaProducer(){

        //create producer properties
        String bootstrapServers = "127.0.0.1:9092";

        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //create safe producer
        properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all");
        properties.setProperty(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));
        properties.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5");

        //create the producer
        return new KafkaProducer<String, String>(properties);
    }

    public static void wait(int ms)
    {
        try{
            Thread.sleep(ms);
        }
        catch(InterruptedException ex){
            Thread.currentThread().interrupt();
        }
    }

}
