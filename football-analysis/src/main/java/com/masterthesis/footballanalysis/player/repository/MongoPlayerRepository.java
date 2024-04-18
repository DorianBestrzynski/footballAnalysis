package com.masterthesis.footballanalysis.player.repository;

import com.masterthesis.footballanalysis.player.dto.*;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MongoPlayerRepository {
    private final MongoDatabase database;

    public List<TopScorersMongo> getTopScorersInAllLeagues() {
        // Get the collection you want to query
        MongoCollection<Document> collection = database.getCollection("player_appearances");

        // Define your aggregation pipeline
        List<Document> pipeline = Arrays.asList(new Document("$unwind", "$appearances"),
                new Document("$lookup",
                        new Document("from", "stats")
                                .append("let",
                                        new Document("game_id", "$appearances.gameID")
                                                .append("league_id", "$appearances.leagueID"))
                                .append("pipeline", Arrays.asList(new Document("$unwind", "$seasons"),
                                        new Document("$unwind", "$seasons.games"),
                                        new Document("$match",
                                                new Document("$expr",
                                                        new Document("$and", Arrays.asList(new Document("$eq", Arrays.asList("$seasons.games.gameID", "$$game_id")),
                                                                new Document("$eq", Arrays.asList("$leagueID", "$$league_id")))))),
                                        new Document("$project",
                                                new Document("season", "$seasons.season")
                                                        .append("leagueName", "$name")
                                                        .append("gameID", "$seasons.games.gameID"))))
                                .append("as", "gameDetails")),
                new Document("$unwind", "$gameDetails"),
                new Document("$group",
                        new Document("_id",
                                new Document("PlayerName", "$name")
                                        .append("LeagueName", "$gameDetails.leagueName"))
                                .append("TotalGoals",
                                        new Document("$sum", "$appearances.goals"))
                                .append("TotalAssists",
                                        new Document("$sum", "$appearances.assists"))
                                .append("Season",
                                        new Document("$addToSet", "$gameDetails.season"))),
                new Document("$sort",
                        new Document("TotalGoals", -1L)
                                .append("TotalAssists", -1L)),
                new Document("$project",
                        new Document("_id", 0L)
                                .append("PlayerName", "$_id.PlayerName")
                                .append("LeagueName", "$_id.LeagueName")
                                .append("TotalGoals", 1L)
                                .append("TotalAssists", 1L)
                                .append("Season", 1L)));

        // Execute the aggregation pipeline
        AggregateIterable<Document> result = collection.aggregate(pipeline);
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
                        new Document("from", "player_appearances")
                                .append("localField", "shooterID")
                                .append("foreignField", "playerID")
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

    public List<PlayerAssistsStats> getPlayerAssists() {
        // Get the collection you want to query
        MongoCollection<Document> collection = database.getCollection("player_appearances");

        // Define your aggregation pipeline
        List<Document> pipeline = Arrays.asList(new Document("$unwind",
                        new Document("path", "$appearances")
                                .append("preserveNullAndEmptyArrays", false)),
                new Document("$group",
                        new Document("_id", "$name")
                                .append("TotalAssists",
                                        new Document("$sum", "$appearances.assists"))
                                .append("GamesPlayed",
                                        new Document("$sum", 1L))),
                new Document("$match",
                        new Document("GamesPlayed",
                                new Document("$gt", 5L))),
                new Document("$project",
                        new Document("PlayerName", "$_id")
                                .append("TotalAssists", 1L)
                                .append("GamesPlayed", 1L)
                                .append("AvgAssistsPerGame",
                                        new Document("$round", Arrays.asList(new Document("$divide", Arrays.asList("$TotalAssists", "$GamesPlayed")), 2L)))),
                new Document("$sort",
                        new Document("AvgAssistsPerGame", -1L)));

        // Execute the aggregation pipeline
        AggregateIterable<Document> result = collection.aggregate(pipeline);
        List<PlayerAssistsStats> playerAssistsStats = new ArrayList<>();
        for (Document doc : result) {
            PlayerAssistsStats playerAssists = new PlayerAssistsStats();
            // Manual mapping from Document to GameStats
            playerAssists.setPlayerName(doc.getString("PlayerName"));
            playerAssists.setTotalAssists(doc.getInteger("TotalAssists"));
            playerAssists.setTotalGames(doc.getLong("GamesPlayed"));
            playerAssists.setAvgAssistsPerGame(doc.getDouble("AvgAssistsPerGame"));
            playerAssistsStats.add(playerAssists);
        }

        return playerAssistsStats;
    }

    public void updatePlayer(Player player) {
        MongoCollection<Document> collection = database.getCollection("player_appearances");

        // Insert or update the player document into the collection
        var filter = new Document("playerID", player.getPlayerId());
        var  update = new Document("$set", player(player));
        UpdateOptions options = new UpdateOptions().upsert(true); // This will insert the document if it doesn't exist, or update it if it does

        collection.updateOne(filter, update, options);
    }

    public void updatePlayers(List<Player> players) {
        MongoCollection<Document> collection = database.getCollection("player_appearances");
        List<WriteModel<Document>> updates = new ArrayList<>();

        for (Player player : players) {
            // Create the filter and update operation for each player
            var filter = Filters.eq("playerID", player.getPlayerId());
            var update = Updates.set("player", player(player));
            UpdateOptions options = new UpdateOptions().upsert(true);

            // Add to the list of update operations
            updates.add(new UpdateOneModel<>(filter, update, options));
        }

        // Execute the bulk write operation
        if (!updates.isEmpty()) {
            collection.bulkWrite(updates);
        }
    }

    private Document player(Player player) {
        return new Document("playerID", player.getPlayerId())
                .append("name", player.getName())
                .append("appearances", appearances(player));
    }

    private List<Document> appearances(Player player) {
        List<Document> appearancesDocs = new ArrayList<>();
        for (PlayerAppearance appearance : player.getAppearances()) {
            Document appearanceDoc = new Document("gameID", appearance.getGameID())
                    .append("leagueID", appearance.getLeagueID())
                    .append("time", appearance.getTime())
                    .append("goals", appearance.getGoals())
                    .append("ownGoals", appearance.getOwnGoals())
                    .append("xGoals", appearance.getXGoals())
                    .append("assists", appearance.getAssists())
                    .append("position", appearance.getPosition())
                    .append("positionOrder", appearance.getPositionOrder())
                    .append("xAssists", appearance.getXAssists())
                    .append("shots", appearance.getShots())
                    .append("keyPasses", appearance.getKeyPasses())
                    .append("yellowCards", appearance.getYellowCards())
                    .append("redCards", appearance.getRedCards())
                    .append("xGoalsChain", appearance.getXGoalsChain())
                    .append("xGoalsBuildup", appearance.getXGoalsBuildup());
            appearancesDocs.add(appearanceDoc);
        }
        return appearancesDocs;
    }
}
