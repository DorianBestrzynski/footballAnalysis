package com.masterthesis.footballanalysis.configuration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class MongoDbConfig {

    @Bean
    MongoDatabase create() {
        var connectionString = new ConnectionString("mongodb://root:password@localhost:27017/");

        var settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .applyToSocketSettings(builder ->
                        builder.readTimeout(3, TimeUnit.HOURS)) // example to set socket read timeout
                .build();

        MongoClient mongoClient = MongoClients.create(settings);
        return mongoClient.getDatabase("FootballAnalysis");
    }
}
