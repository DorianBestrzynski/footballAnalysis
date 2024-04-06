package com.masterthesis.footballanalysis.player.team.repository;

import com.masterthesis.footballanalysis.player.team.dto.GameStatsMongo;
import com.mongodb.client.*;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MongoTeamRepository {
    private final MongoDatabase database;

    public List<GameStatsMongo> getMostIntenseMatches() {
        // Get the collection you want to query
        MongoCollection<Document> collection = database.getCollection("stats");

        // Define your aggregation pipeline
        List<Document> pipeline = Arrays.asList(
                new Document("$unwind", new Document("path", "$seasons")),
                new Document("$unwind", new Document("path", "$seasons.games")),
                new Document("$project",
                        new Document("LeagueName", "$name")
                                .append("Match",
                                        new Document("$concat", Arrays.asList("$seasons.games.homeTeam.name", " vs ", "$seasons.games.awayTeam.name")))
                                .append("date", "$seasons.games.date")
                                .append("TotalShots",
                                        new Document("$add", Arrays.asList("$seasons.games.homeTeam.teamStats.shots", "$seasons.games.awayTeam.teamStats.shots")))),
                new Document("$sort", new Document("TotalShots", -1L))
        );

        // Execute the aggregation pipeline
        AggregateIterable<Document> result = collection.aggregate(pipeline);

        List<GameStatsMongo> gameStatsList = new ArrayList<>();
        for (Document doc : result) {
            GameStatsMongo gameStats = new GameStatsMongo();
            // Manual mapping from Document to GameStats
            gameStats.setLeagueName(doc.getString("LeagueName"));
            gameStats.setMatchName(doc.getString("Match"));
            gameStats.setDate(doc.getString("date"));
            gameStats.setShots(doc.getInteger("TotalShots"));

            gameStatsList.add(gameStats);
        }

        return gameStatsList;
    }
}
