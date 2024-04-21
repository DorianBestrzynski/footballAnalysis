package com.masterthesis.footballanalysis.version_2.repository;


import com.masterthesis.footballanalysis.version_2.dto.*;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.RequiredArgsConstructor;
import org.bson.BsonNull;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository("MongoDbReadRepositoryV2")
@RequiredArgsConstructor
public class MongoDbReadRepository {
    private final MongoDatabase database;

    public List<Query1DTOMongo> query1(Timestamp date) {
        MongoCollection<Document> collection = database.getCollection("Statistics");

        List<Document> pipeline = Arrays.asList(new Document("$match",
                        new Document("date",
                                new Document("$gt", date.toString()))),
                new Document("$project",
                        new Document("gameID", 1L)
                                .append("leagueName", 1L)
                                .append("season", 1L)
                                .append("date", 1L)
                                .append("homeTeamID", 1L)
                                .append("awayTeamID", 1L)
                                .append("homeGoals", 1L)
                                .append("awayGoals", 1L)));

        // Execute the aggregation pipeline
        AggregateIterable<Document> result = collection.aggregate(pipeline);

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
        MongoCollection<Document> collection = database.getCollection("Statistics");
        List<Document> pipeline = Arrays.asList(new Document("$unwind",
                        new Document("path", "$appearances")
                                .append("preserveNullAndEmptyArrays", false)),
                new Document("$project",
                        new Document("goals", "$appearances.goals")
                                .append("ownGoals", "$appearances.ownGoals")
                                .append("shots", "$appearances.shots")
                                .append("assists", "$appearances.assists")),
                new Document("$sort",
                        new Document("goals", -1L)
                                .append("assists", -1L)
                                .append("shots", -1L)
                                .append("ownGoals", -1L)));

        // Execute the aggregation pipeline
        AggregateIterable<Document> result = collection.aggregate(pipeline);

        List<Query2DTO> query2List = new ArrayList<>();
        for (Document doc : result) {
            Query2DTO query2 = new Query2DTO();
            query2.setGoals(doc.getInteger("goals"));
            query2.setOwnGoals(doc.getInteger("ownGoals"));
            query2.setShots(doc.getInteger("shots"));
            query2.setAssists(doc.getInteger("assists"));
            query2List.add(query2);
        }
        return query2List;
    }

    public List<Query3DTOMongo> query3() {
        MongoCollection<Document> collection = database.getCollection("Statistics");
        List<Document> pipeline = Arrays.asList(new Document("$unwind",
                        new Document("path", "$shots")
                                .append("preserveNullAndEmptyArrays", false)),
                new Document("$project",
                        new Document("date", "$date")
                                .append("season", "$season")
                                .append("minute", "$shots.minute")
                                .append("situation", "$shots.situation")
                                .append("shotType", "$shots.shotType")
                                .append("shotResult", "$shots.shotResult")));

        // Execute the aggregation pipeline
        AggregateIterable<Document> result = collection.aggregate(pipeline);

        List<Query3DTOMongo> query3DTOMongoList = new ArrayList<>();
        for (Document doc : result) {
            Query3DTOMongo query3 = new Query3DTOMongo();
            query3.setDate(doc.getString("date"));
            query3.setSeason(doc.getInteger("season"));
            query3.setMinute(doc.getInteger("minute"));
            query3.setSituation(doc.getString("situation"));
            query3.setShotType(doc.getString("shotType"));
            query3.setShotResult(doc.getString("shotResult"));
            query3DTOMongoList.add(query3);
        }
        return query3DTOMongoList;
    }

    public List<Query4DTOMongo> query4() {
        MongoCollection<Document> collection = database.getCollection("Statistics");

        List<Document> pipeline = Arrays.asList(new Document("$unwind",
                        new Document("path", "$appearances")
                                .append("preserveNullAndEmptyArrays", false)),
                new Document("$unwind",
                        new Document("path", "$shots")
                                .append("preserveNullAndEmptyArrays", false)),
                new Document("$project",
                        new Document("date", "$date")
                                .append("season", "$season")
                                .append("match",
                                        new Document("$concat", Arrays.asList("$homeTeam.name", " vs ", "$awayTeam.name")))
                                .append("leagueName", "$leagueName")
                                .append("result",
                                        new Document("$concat", Arrays.asList(new Document("$toString", "$homeTeam.teamStats.goals_home"), " : ",
                                                new Document("$toString", "$awayTeam.teamStats.goals_away"))))
                                .append("minute", "$shots.minute")
                                .append("situation", "$shots.situation")
                                .append("shotResult", "$shots.shotResult")
                                .append("playerName", "$shots. name")
                                .append("playerGoals", "$appearances.goals")
                                .append("playerShots", "$appearances.shots")));

        // Execute the aggregation pipeline
        AggregateIterable<Document> result = collection.aggregate(pipeline);

        List<Query4DTOMongo> query4DTOMongos = new ArrayList<>();
        for (Document doc : result) {
            Query4DTOMongo query4 = new Query4DTOMongo();
            query4.setDate(doc.getString("date"));
            query4.setSeason(doc.getInteger("season"));
            query4.setMatch(doc.getString("match"));
            query4.setLeagueName(doc.getString("leagueName"));
            query4.setResult(doc.getString("result"));
            query4.setMinute(doc.getInteger("minute"));
            query4.setSituation(doc.getString("situation"));
            query4.setShotResult(doc.getString("shotResult"));
            query4.setPlayerName(doc.getString("playerName"));
            query4.setPlayerGoals(doc.getInteger("playerGoals"));
            query4.setPlayerShots(doc.getInteger("playerShots"));
            query4DTOMongos.add(query4);
        }
        return query4DTOMongos;
    }

    public List<Query5DTOMongo> query5() {
        MongoCollection<Document> collection = database.getCollection("Statistics");

        List<Document> pipeline = Arrays.asList(new Document("$unwind",
                        new Document("path", "$shots")
                                .append("preserveNullAndEmptyArrays", false)),
                new Document("$group",
                        new Document("_id",
                                new Document("gameID", "$gameID")
                                        .append("date", "$date")
                                        .append("season", "$season")
                                        .append("homeTeamID", "$homeTeamID")
                                        .append("awayTeamID", "$awayTeamID"))
                                .append("totalShots",
                                        new Document("$sum", 1L))
                                .append("goalsScored",
                                        new Document("$sum",
                                                new Document("$cond", Arrays.asList(new Document("$eq", Arrays.asList("$shots.shotResult", "Goal")), 1L, 0L))))),
                new Document("$project",
                        new Document("gameID", "$_id.gameID")
                                .append("date", "$_id.date")
                                .append("season", "$_id.season")
                                .append("totalShots", 1L)
                                .append("goalsScored", 1L)
                                .append("_id", 0L)),
                new Document("$sort",
                        new Document("date", -1L)
                                .append("goalsScored", -1L)));

        // Execute the aggregation pipeline
        AggregateIterable<Document> result = collection.aggregate(pipeline);

        List<Query5DTOMongo> query5DTOs = new ArrayList<>();
        for (Document doc : result) {
            Query5DTOMongo query5 = new Query5DTOMongo();
            query5.setGameId(doc.getInteger("gameID"));
            query5.setDate(doc.getString("date"));
            query5.setSeason(doc.getInteger("season"));
            query5.setTotalShots(doc.getInteger("totalShots"));
            query5.setGoalsScored(doc.getInteger("goalsScored"));
            query5DTOs.add(query5);
        }
        return query5DTOs;
    }

    public List<Query6DTOMongo> query6() {
        MongoCollection<Document> collection = database.getCollection("Statistics");

        List<Document> pipeline = Arrays.asList(new Document("$unwind",
                        new Document("path", "$appearances")
                                .append("preserveNullAndEmptyArrays", false)),
                new Document("$unwind",
                        new Document("path", "$shots")
                                .append("preserveNullAndEmptyArrays", false)),
                new Document("$group",
                        new Document("_id",
                                new Document("gameID", "$gameID")
                                        .append("date", "$date")
                                        .append("season", "$season")
                                        .append("homeTeamName", "$homeTeam.name")
                                        .append("awayTeamName", "$awayTeam.name")
                                        .append("leagueName", "$leagueName")
                                        .append("homeGoals", "$homeTeam.teamStats.goals_home")
                                        .append("awayGoals", "$awayTeam.teamStats.goals_away")
                                        .append("minute", "$shots.minute")
                                        .append("situation", "$shots.situation")
                                        .append("shotResult", "$shots.shotResult")
                                        .append("playerName", "$shots.name")
                                        .append("playerGoals", "$appearances.goals")
                                        .append("playerShots", "$appearances.shots"))
                                .append("totalShots",
                                        new Document("$sum", 1L))
                                .append("totalGoals",
                                        new Document("$sum",
                                                new Document("$cond", Arrays.asList(new Document("$eq", Arrays.asList("$shots.shotResult", "Goal")), 1L, 0L))))
                                .append("avgShotsPerPlayer",
                                        new Document("$avg", "$appearances.shots"))
                                .append("startingPlayers",
                                        new Document("$sum",
                                                new Document("$cond", Arrays.asList(new Document("$eq", Arrays.asList("$appearances.substitutein",
                                                        new BsonNull())), 1L, 0L))))
                                .append("substitutedPlayers",
                                        new Document("$sum",
                                                new Document("$cond", Arrays.asList(new Document("$ne", Arrays.asList("$appearances.substitutein",
                                                        new BsonNull())), 1L, 0L))))),
                new Document("$project",
                        new Document("date", "$_id.date")
                                .append("season", "$_id.season")
                                .append("match",
                                        new Document("$concat", Arrays.asList("$_id.homeTeamName", " vs ", "$_id.awayTeamName")))
                                .append("leagueName", "$_id.leagueName")
                                .append("result",
                                        new Document("$concat", Arrays.asList(new Document("$toString", "$_id.homeGoals"), " : ",
                                                new Document("$toString", "$_id.awayGoals"))))
                                .append("minute", "$_id.minute")
                                .append("situation", "$_id.situation")
                                .append("shotResult", "$_id.shotResult")
                                .append("playerName", "$_id.playerName")
                                .append("playerGoals", "$_id.playerGoals")
                                .append("playerShots", "$_id.playerShots")
                                .append("totalShots", 1L)
                                .append("totalGoals", 1L)
                                .append("avgShotsPerPlayer", 1L)
                                .append("startingPlayers", 1L)
                                .append("substitutedPlayers", 1L)),
                new Document("$sort",
                        new Document("date", -1L)));

        // Execute the aggregation pipeline
        AggregateIterable<Document> result = collection.aggregate(pipeline);

        List<Query6DTOMongo> query6DTOS = new ArrayList<>();
        for (Document doc : result) {
            Query6DTOMongo query6 = new Query6DTOMongo();
            query6.setDate(doc.getString("date"));
            query6.setSeason(doc.getInteger("season"));
            query6.setMatch(doc.getString("match"));
            query6.setLeagueName(doc.getString("leagueName"));
            query6.setResult(doc.getString("result"));
            query6.setAvgShotsPerPlayer(doc.getDouble("avgShotsPerPlayer"));
            query6.setTotalShots(doc.getInteger("totalShots"));
            query6.setTotalGoals(doc.getInteger("totalGoals"));
            query6.setMinute(doc.getInteger("minute"));
            query6.setSituation(doc.getString("situation"));
            query6.setShotResult(doc.getString("shotResult"));
            query6.setPlayerName(doc.getString("playerName"));
            query6.setPlayerGoals(doc.getInteger("playerGoals"));
            query6.setPlayerShots(doc.getInteger("playerShots"));
            query6.setStartingPlayers(doc.getInteger("startingPlayers"));
            query6.setSubstitutedPlayers(doc.getInteger("substitutedPlayers"));
            query6DTOS.add(query6);
        }
        return query6DTOS;
    }
}
