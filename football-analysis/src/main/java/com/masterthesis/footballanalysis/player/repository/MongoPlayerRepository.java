package com.masterthesis.footballanalysis.player.repository;

import com.masterthesis.footballanalysis.player.dto.*;
import com.masterthesis.footballanalysis.player.team.dto.GameStatsMongo;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MongoPlayerRepository {
    private final MongoDatabase database;

    public List<TopScorersMongo> getTopScorersInAllLeagues() {
        // Get the collection you want to query
        MongoCollection<Document> collection = database.getCollection("flattened_appearances");

        // Define your aggregation pipeline
        List<Document> pipeline = Arrays.asList(new Document("$lookup",
                        new Document("from", "stats")
                                .append("let",
                                        new Document("local_gameID", "$gameID"))
                                .append("pipeline", Arrays.asList(new Document("$unwind", "$seasons"),
                                        new Document("$unwind", "$seasons.games"),
                                        new Document("$match",
                                                new Document("$expr",
                                                        new Document("$and", Arrays.asList(new Document("$eq", Arrays.asList("$seasons.games.gameID", "$$local_gameID"))))))))
                                .append("as", "gameInfo")),
                new Document("$unwind",
                        new Document("path", "$gameInfo")));

        // Execute the aggregation pipeline
        AggregateIterable<Document> result = collection.aggregate(pipeline);
        System.out.println("test");
        List<TopScorersMongo> topScorerList = new ArrayList<>();
        for (Document doc : result) {
            TopScorersMongo topScorersMongo = new TopScorersMongo();
            // Manual mapping from Document to GameStats
            topScorersMongo.setSeason(doc.getInteger("Season"));
            topScorersMongo.setLeagueName(doc.getString("LeagueName"));
            topScorersMongo.setPlayerName(doc.getString("PlayerName"));
            topScorersMongo.setTotalGoals(doc.getInteger("TotalGoals"));
            topScorersMongo.setTotalAssists(doc.getInteger("TotalAssists"));

            topScorerList.add(topScorersMongo);
        }

        return topScorerList;
    }

    public List<PlayerShotsMongo> getShots() {
        // Get the collection you want to query
        MongoCollection<Document> collection = database.getCollection("shots");

        // Define your aggregation pipeline
        List<Document> pipeline = Arrays.asList(new Document("$lookup",
                        new Document("from", "flattened_appearances")
                                .append("localField", "shooterID")
                                .append("foreignField", "playerId")
                                .append("as", "playerInfo")),
                new Document("$addFields",
                        new Document("playerName",
                                new Document("$arrayElemAt", Arrays.asList("$playerInfo.name", 0L)))),
                new Document("$group",
                        new Document("_id", "$shooterID")
                                .append("count",
                                        new Document("$sum", 1L))
                                .append("playerName",
                                        new Document("$first", "$playerName"))),
                new Document("$sort",
                        new Document("count", -1L)));

        // Execute the aggregation pipeline
        AggregateIterable<Document> result = collection.aggregate(pipeline);
        System.out.println("test");
        List<PlayerShotsMongo> topScorerList = new ArrayList<>();
        for (Document doc : result) {
            PlayerShotsMongo topScorersMongo = new PlayerShotsMongo();
            // Manual mapping from Document to GameStats
            topScorersMongo.setName(doc.getString("playerName"));
            topScorersMongo.setCount(doc.getLong("count"));
            topScorerList.add(topScorersMongo);
        }

        return topScorerList;
    }
}
