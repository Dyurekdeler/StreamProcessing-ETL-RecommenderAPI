package com.dyurekdeler.kafka;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;

public class ProductViewConsumer {


    public static void main(String[] args){
        new ProductViewConsumer().run();
    }

    private ProductViewConsumer(){}

    private void run(){

        Logger logger = LoggerFactory.getLogger(ProductViewConsumer.class);

        String bootstrapServers = "127.0.0.1:9092";
        String groupId = "kafka-stream-processing";
        String topic = "topic_clickevent";

        //latch for dealing with multiple threads
        CountDownLatch latch = new CountDownLatch(1);

        // create the consumer runnable
        logger.info("Creating the consumer thread");
        ConsumerRunnable consumerRunnable = new ConsumerRunnable(latch, bootstrapServers, groupId, topic);

        //start the thread
        Thread consumerThread = new Thread(consumerRunnable);
        consumerThread.start();

        //add a shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread( () -> {
            logger.info("Caught shutdown hook");
            consumerRunnable.shutdown();
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info("Application has exited!");
        }));

        //wait until app is over
        try{
            latch.await();
        }
        catch (InterruptedException e){
            logger.error("Application is interrupted! ", e);
        }
        finally {
            logger.info("Application is closing!");
        }

    }

    private static Map<String, String> extractFields(String viewEvent){

        Map<String, String> fields = new HashMap<>();
        JsonObject viewEventJO = JsonParser.parseString(viewEvent).getAsJsonObject();
        JsonObject propertiesJO = viewEventJO.getAsJsonObject("properties");
        JsonObject contextJO = viewEventJO.getAsJsonObject("context");

        fields.put("event", viewEventJO.get("event").getAsString());
        fields.put("user_id", viewEventJO.get("userid").getAsString());
        fields.put("message_id", viewEventJO.get("messageid").getAsString());
        fields.put("product_id", propertiesJO.get("productid").getAsString());
        fields.put("source", contextJO.get("source").getAsString());
        fields.put("click_timestamp", viewEventJO.get("click_timestamp").getAsString());

        return fields;
    }

    private static void insertToPostgres(Map<String, String> fields){

        String url = "jdbc:postgresql://localhost:5432/bestseller-db";
        String user = "postgres";
        String password = "123456";

        String query = "INSERT INTO public.product_view_history(\n" +
                "\t event, message_id, user_id, product_id, source, click_timestamp)\n" +
                "\tVALUES (?, ?, ?, ?, ?, ?);";

        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, fields.get("event"));
            pst.setString(2, fields.get("message_id"));
            pst.setString(3, fields.get("user_id"));
            pst.setString(4, fields.get("product_id"));
            pst.setString(5, fields.get("source"));
            pst.setTimestamp(6, Timestamp.valueOf(fields.get("click_timestamp")));
            pst.executeUpdate();

        } catch (SQLException ex) {

            java.util.logging.Logger lgr = java.util.logging.Logger.getLogger(ProductViewConsumer.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static class ConsumerRunnable implements Runnable {

        private CountDownLatch latch;
        private KafkaConsumer<String, String> consumer;
        private Logger logger = LoggerFactory.getLogger(ProductViewConsumer.class);

        public ConsumerRunnable(CountDownLatch latch, String bootstrapServers, String groupId, String topic){
            this.latch = latch;

            Properties properties = new Properties();
            properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
            properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

            //create consumer
            consumer = new KafkaConsumer<String, String>(properties);

            //subs consumer to related topic
            consumer.subscribe(Arrays.asList(topic));

        }

        @Override
        public void run() {
            try{
                //poll for new data
                while(true){

                    ConsumerRecords<String, String> records =  consumer.poll(Duration.ofMillis(100));

                    for(ConsumerRecord<String, String> record: records){
                        //logger.info("value: " + record.value());
                        Map<String, String> fields = extractFields(record.value());

                        //add id field for idempotent
                        insertToPostgres(fields);
                        logger.info("Id: " + fields.get("message_id"));

                    }
                }

            }
            catch(WakeupException e){
                logger.info("Received shutdown signal!");

            }
            finally {
                consumer.close();
                //tell main code, done with the consumer
                latch.countDown();
            }

        }

        public void shutdown(){
            //to interrupt consumer.poll(), it will throw the exception WakeUpException
            consumer.wakeup();
        }
    }
}
