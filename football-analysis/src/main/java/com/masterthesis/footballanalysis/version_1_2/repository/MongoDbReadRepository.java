package com.masterthesis.footballanalysis.version_1_2.repository;


import com.masterthesis.footballanalysis.version_1_2.dto.*;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository("MongoDbReadRepositoryV1_2")
@RequiredArgsConstructor
public class MongoDbReadRepository {
    private static final String STATISTICS_COLLECTION = "Statistics";

    private final MongoDatabase database;

    public List<Query1DTOMongo> query1(int awayGoals) {
        MongoCollection<Document> collection = database.getCollection(STATISTICS_COLLECTION);

        FindIterable<Document> result = collection.find(new Document("awayGoals", new Document("$gt", awayGoals)))
                .projection(new Document("gameID", 1L)
                        .append("leagueName", 1L)
                        .append("season", 1L)
                        .append("date", 1L)
                        .append("homeTeamID", 1L)
                        .append("awayTeamID", 1L)
                        .append("homeGoals", 1L)
                        .append("awayGoals", 1L));

        List<Query1DTOMongo> query1List = new ArrayList<>();
        for (Document doc : result) {
            Query1DTOMongo query1 = new Query1DTOMongo();
            query1.setGameId(doc.getInteger("gameID"));
            query1.setLeagueName(doc.getString("leagueName"));
            query1.setSeason(doc.getInteger("season"));
            query1.setDate(doc.getString("date"));
            query1.setHomeGoals(doc.getInteger("homeGoals"));
            query1.setAwayGoals(doc.getInteger("awayGoals"));
            query1List.add(query1);
        }
        return query1List;
    }

    public List<Query2DTO> query2() {
        MongoCollection<Document> collection = database.getCollection(STATISTICS_COLLECTION);
        FindIterable<Document> result = collection.find(new Document("homeTeam.teamStats.location_home", "h"))
                .projection(new Document("homeTeam.teamStats.location_home", 1)
                        .append("homeTeam.teamStats.goals_home", 1)
                        .append("homeTeam.teamStats.xGoals_home", 1)
                        .append("homeTeam.teamStats.shots_home", 1)
                        .append("homeTeam.teamStats.shotsOnTarget_home", 1)
                        .append("homeTeam.teamStats.deep_home", 1));

        List<Query2DTO> query2List = new ArrayList<>();
        for (Document doc : result) {
            Query2DTO query2 = new Query2DTO();
            Double xGoalsNumber = doc.get("xGoals", Number.class).doubleValue(); // Get the value as a Number
            query2.setLocation(doc.getString("location"));
            query2.setGoals(doc.getInteger("goals"));
            query2.setShots(doc.getInteger("shots"));
            query2.setShotsontarget(doc.getInteger("shotsOnTarget"));
            query2.setXGoals(xGoalsNumber);
            query2.setDeep(doc.getInteger("deep"));
            query2List.add(query2);
        }
        return query2List;
    }

    public List<Query3DTOMongo> query3() {
        MongoCollection<Document> collection = database.getCollection(STATISTICS_COLLECTION);
        List<Document> pipeline = Arrays.asList(new Document("$unwind",
                        new Document("path", "$shots")
                                .append("preserveNullAndEmptyArrays", false)),
                new Document("$project",
                        new Document("playerID", 1L)
                                .append("name", 1L)
                                .append("minute", "$shots.minute")
                                .append("situation", "$shots.situation")
                                .append("lastAction", "$shots.lastAction")
                                .append("shotType", "$shots.shotType")
                                .append("shotResult", "$shots.shotResult")));

        // Execute the aggregation pipeline
        AggregateIterable<Document> result = collection.aggregate(pipeline);

        List<Query3DTOMongo> query3DTOMongoList = new ArrayList<>();
        for (Document doc : result) {
            Query3DTOMongo query3 = new Query3DTOMongo();
            query3.setPlayerId(doc.getInteger("playerID"));
            query3.setName(doc.getString("name"));
            query3.setMinute(doc.getInteger("minute"));
            query3.setSituation(doc.getString("situation"));
            query3.setLastAction(doc.getString("lastAction"));
            query3.setShotType(doc.getString("shotType"));
            query3.setShotResult(doc.getString("shotResult"));
            query3DTOMongoList.add(query3);
        }
        return query3DTOMongoList;
    }

    public List<Query4DTOMongo> query4() {
        MongoCollection<Document> collection = database.getCollection(STATISTICS_COLLECTION);

        List<Document> pipeline = Arrays.asList(new Document("$project",
                        new Document("LeagueName", "$leagueName")
                                .append("Match",
                                        new Document("$concat", Arrays.asList("$homeTeam.name", " vs ", "$awayTeam.name")))
                                .append("Date", "$date")
                                .append("Shots", "$homeTeam.teamStats.shots_home")),
                new Document("$sort",
                        new Document("Shots", -1L)));

        // Execute the aggregation pipeline
        AggregateIterable<Document> result = collection.aggregate(pipeline);

        List<Query4DTOMongo> query4DTOMongos = new ArrayList<>();
        for (Document doc : result) {
            Query4DTOMongo query4 = new Query4DTOMongo();
            query4.setLeagueName(doc.getString("LeagueName"));
            query4.setMatch(doc.getString("Match"));
            query4.setDate(doc.getString("Date"));
            query4.setHomeShots(doc.getInteger("Shots"));
            query4DTOMongos.add(query4);
        }
        return query4DTOMongos;
    }

    public List<Query5DTO> query5() {
        MongoCollection<Document> collection = database.getCollection(STATISTICS_COLLECTION);

        List<Document> pipeline = Arrays.asList(new Document("$unwind",
                        new Document("path", "$shots")
                                .append("preserveNullAndEmptyArrays", false)),
                new Document("$project",
                        new Document("gameId", "$gameID")
                                .append("season", "$season")
                                .append("shotType", "$shots.shotType")
                                .append("shotResult", "$shots.shotResult")));

        // Execute the aggregation pipeline
        AggregateIterable<Document> result = collection.aggregate(pipeline);

        List<Query5DTO> query5DTOs = new ArrayList<>();
        for (Document doc : result) {
            Query5DTO query5 = new Query5DTO();
            query5.setGameId(doc.getInteger("gameId"));
            query5.setSeason(doc.getInteger("season"));
            query5.setShotType(doc.getString("shotType"));
            query5.setShotResult(doc.getString("shotResult"));
            query5DTOs.add(query5);
        }
        return query5DTOs;
    }

    public List<Query6DTO> query6() {
        MongoCollection<Document> collection = database.getCollection(STATISTICS_COLLECTION);

        List<Document> pipeline = Arrays.asList(new Document("$unwind",
                        new Document("path", "$appearances")
                                .append("preserveNullAndEmptyArrays", false)),
                new Document("$project",
                        new Document("playerName", "$appearances. name")
                                .append("season", "$season")
                                .append("leagueName", "$leagueName")
                                .append("goals", "$appearances.goals")));

        // Execute the aggregation pipeline
        AggregateIterable<Document> result = collection.aggregate(pipeline);

        List<Query6DTO> query6DTOS = new ArrayList<>();
        for (Document doc : result) {
            Query6DTO query6 = new Query6DTO();
            query6.setPlayerName(doc.getString("playerName"));
            query6.setSeason(doc.getInteger("season"));
            query6.setLeagueName(doc.getString("leagueName"));
            query6.setGoals(doc.getInteger("goals"));
            query6DTOS.add(query6);
        }
        return query6DTOS;
    }

    public List<Query7DTO> query7() {
        // Get the collection you want to query
        MongoCollection<Document> collection = database.getCollection(STATISTICS_COLLECTION);

        // Define your aggregation pipeline
        List<Document> pipeline = Arrays.asList(new Document("$unwind",
                        new Document("path", "$appearances")
                                .append("preserveNullAndEmptyArrays", false)),
                new Document("$group",
                        new Document("_id", "$appearances. name")
                                .append("TotalAssists",
                                        new Document("$sum", "$appearances.assists"))
                                .append("GamePlayed",
                                        new Document("$sum", 1L))),
                new Document("$project",
                        new Document("_id", 0L)
                                .append("PlayerName", "$_id")
                                .append("TotalAssists", 1L)
                                .append("GamePlayed", 1L)
                                .append("AvgAssistsPerGame",
                                        new Document("$round", Arrays.asList(new Document("$divide", Arrays.asList("$TotalAssists", "$GamePlayed")), 2L)))),
                new Document("$match",
                        new Document("GamePlayed",
                                new Document("$gt", 5L))),
                new Document("$sort",
                        new Document("AvgAssistsPerGame", -1L)));

        // Execute the aggregation pipeline
        AggregateIterable<Document> result = collection.aggregate(pipeline);
        List<Query7DTO> query7DTOS = new ArrayList<>();
        for (Document doc : result) {
            Query7DTO query7 = new Query7DTO();
            // Manual mapping from Document to GameStats
            query7.setPlayerName(doc.getString("PlayerName"));
            query7.setTotalAssists(doc.getInteger("TotalAssists"));
            query7.setTotalGames(doc.getLong("GamePlayed"));
            query7.setAvgAssistsPerGame(doc.getDouble("AvgAssistsPerGame"));
            query7DTOS.add(query7);
        }

        return query7DTOS;
    }

    public List<Query8DTOMongo> query8() {
        // Get the collection you want to query
        MongoCollection<Document> collection = database.getCollection(STATISTICS_COLLECTION);

        // Define your aggregation pipeline
        List<Document> pipeline = Arrays.asList(new Document("$project",
                        new Document("LeagueName", "$leagueName")
                                .append("Match",
                                        new Document("$concat", Arrays.asList("$homeTeam.name", " vs ", "$awayTeam.name")))
                                .append("TotalShots",
                                        new Document("$sum", Arrays.asList("$homeTeam.teamStats.shots_home", "$awayTeam.teamStats.shots_away")))
                                .append("GameID", "$gameID")
                                .append("Date", "$date")),
                new Document("$group",
                        new Document("_id",
                                new Document("LeagueName", "$LeagueName")
                                        .append("Match", "$Match")
                                        .append("Date", "$Date")
                                        .append("GameID", "$GameID"))
                                .append("TotalShots",
                                        new Document("$first", "$TotalShots"))),
                new Document("$project",
                        new Document("_id", 0L)
                                .append("LeagueName", "$_id.LeagueName")
                                .append("Match", "$_id.Match")
                                .append("Date", "$_id.Date")
                                .append("TotalShots", 1L)
                                .append("GameID", "$_id.GameID")),
                new Document("$sort",
                        new Document("TotalShots", -1L)));

        // Execute the aggregation pipeline
        AggregateIterable<Document> result = collection.aggregate(pipeline);

        List<Query8DTOMongo> query8DTOMongos = new ArrayList<>();
        for (Document doc : result) {
            Query8DTOMongo gameStats = new Query8DTOMongo();
            // Manual mapping from Document to GameStats
            gameStats.setLeagueName(doc.getString("LeagueName"));
            gameStats.setMatchName(doc.getString("Match"));
            gameStats.setDate(doc.getString("Date"));
            gameStats.setShots(doc.getInteger("TotalShots"));

            query8DTOMongos.add(gameStats);
        }
        return query8DTOMongos;
    }

    public List<Query9DTO> query9() {
        // Get the collection you want to query
        MongoCollection<Document> collection = database.getCollection(STATISTICS_COLLECTION);

        // Define your aggregation pipeline
        List<Document> pipeline = Arrays.asList(new Document("$unwind",
                        new Document("path", "$shots")
                                .append("preserveNullAndEmptyArrays", false)),
                new Document("$group",
                        new Document("_id",
                                new Document("season", "$season"))
                                .append("TotalXGoal",
                                        new Document("$sum", "$shots.xGoal"))),
                new Document("$project",
                        new Document("_id", 0L)
                                .append("Season", "$_id.season")
                                .append("TotalXGoal", 1L)),
                new Document("$sort",
                        new Document("TotalXGoal", -1L)));

        // Execute the aggregation pipeline
        AggregateIterable<Document> result = collection.aggregate(pipeline);

        List<Query9DTO> query9DTOS = new ArrayList<>();
        for (Document doc : result) {
            Query9DTO query9 = new Query9DTO();
            double xGoalsNumber = doc.get("TotalXGoal", Number.class).doubleValue(); // Get the value as a Number
            query9.setSeason(doc.getInteger("Season"));
            query9.setXGoalSum(xGoalsNumber);
            query9DTOS.add(query9);
        }
        return query9DTOS;
    }

    public List<Query10DTO> query10() {
        // Get the collection you want to query
        MongoCollection<Document> collection = database.getCollection(STATISTICS_COLLECTION);

        // Define your aggregation pipeline
        List<Document> pipeline = Arrays.asList(new Document("$unwind",
                        new Document("path", "$appearances")
                                .append("preserveNullAndEmptyArrays", false)),
                new Document("$group",
                        new Document("_id",
                                new Document("name", "$appearances. name")
                                        .append("season", "$season"))
                                .append("TotalGoals",
                                        new Document("$sum", "$appearances.goals"))),
                new Document("$project",
                        new Document("_id", 0L)
                                .append("PlayerName", "$_id.name")
                                .append("Season", "$_id.season")
                                .append("TotalGoals", 1L)),
                new Document("$sort",
                        new Document("Season", 1L)
                                .append("TotalGoals", -1L)));

        // Execute the aggregation pipeline
        AggregateIterable<Document> result = collection.aggregate(pipeline);

        List<Query10DTO> query10DTOS = new ArrayList<>();
        for (Document doc : result) {
            Query10DTO query10 = new Query10DTO();
            query10.setName(doc.getString("PlayerName"));
            query10.setSeason(doc.getInteger("Season"));
            query10.setGoals(doc.getInteger("TotalGoals"));
            query10DTOS.add(query10);
        }
        return query10DTOS;
    }

    public List<Query11DTO> query11() {
        // Get the collection you want to query
        MongoCollection<Document> collection = database.getCollection("Player_Appearances_Shots");

        // Define your aggregation pipeline
        List<Document> pipeline = Arrays.asList(new Document("$unwind",
                        new Document("path", "$shots")
                                .append("preserveNullAndEmptyArrays", false)),
                new Document("$match",
                        new Document("shots.shotResult", "Goal")),
                new Document("$group",
                        new Document("_id",
                                new Document("name", "$shots. name")
                                        .append("situation", "$shots.situation")
                                        .append("shotResult", "$shots.shotResult"))),
                new Document("$project",
                        new Document("_id", 0L)
                                .append("name", "$_id.name")
                                .append("situation", "$_id.situation")
                                .append("shotResult", "$_id.shotResult")));

        // Execute the aggregation pipeline
        AggregateIterable<Document> result = collection.aggregate(pipeline);

        List<Query11DTO> query11DTOS = new ArrayList<>();
        for (Document doc : result) {
            Query11DTO query11 = new Query11DTO();
            query11.setPlayerName(doc.getString("name"));
            query11.setSituation(doc.getString("situation"));
            query11.setShotResult(doc.getString("shotResult"));
            query11DTOS.add(query11);
        }
        return query11DTOS;
    }
}
