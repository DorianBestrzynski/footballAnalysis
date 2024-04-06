package com.masterthesis.footballanalysis.configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoDbConfig {

    @Bean
    MongoDatabase create() {
        MongoClient mongoClient = MongoClients.create("mongodb://root:password@localhost:27017/");
        return mongoClient.getDatabase("FootballAnalysis");
    }
}
