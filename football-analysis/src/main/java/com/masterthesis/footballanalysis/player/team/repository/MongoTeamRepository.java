package com.masterthesis.footballanalysis.player.team.repository;

import com.masterthesis.footballanalysis.player.team.dto.*;
import com.mongodb.client.*;
import com.mongodb.client.model.*;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mongodb.client.MongoCollection;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;
import static com.mongodb.client.model.Updates.set;

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

    public List<GoalsInMatch> goalsInMatch() {
        // Get the collection you want to query
        MongoCollection<Document> collection = database.getCollection("stats");

        // Define your aggregation pipeline
        List<Document> pipeline = Arrays.asList(new Document("$unwind", "$seasons"),
                new Document("$unwind", "$seasons.games"),
                new Document("$project",
                        new Document("leagueName", "$name")
                                .append("season", "$seasons.season")
                                .append("matchID", "$seasons.games.gameID")
                                .append("totalGoals",
                                        new Document("$sum", Arrays.asList("$seasons.games.homeTeam.teamStats.goals", "$seasons.games.awayTeam.teamStats.goals")))),
                new Document("$sort",
                        new Document("totalGoals", -1L)));

        // Execute the aggregation pipeline
        AggregateIterable<Document> result = collection.aggregate(pipeline);

        List<GoalsInMatch> goalsInMatches = new ArrayList<>();
        for (Document doc : result) {
            GoalsInMatch goalsInMatch = new GoalsInMatch();
            // Manual mapping from Document to GameStats
            goalsInMatch.setSeason(doc.getInteger("season"));
            goalsInMatch.setLeagueName(doc.getString("leagueName"));
            goalsInMatch.setTotalGoals(doc.getInteger("totalGoals"));

            goalsInMatches.add(goalsInMatch);
        }

        return goalsInMatches;
    }

    public List<TeamGoalsPerLeagueAndSeason> getGoalsStatsPerLeagueAndSeason() {
        // Get the collection you want to query
        MongoCollection<Document> collection = database.getCollection("stats");

        // Define your aggregation pipeline
        List<Document> pipeline = Arrays.asList(new Document("$unwind", "$seasons"),
                new Document("$unwind", "$seasons.games"),
                new Document("$project",
                        new Document("leagueName", "$name")
                                .append("season", "$seasons.season")
                                .append("game", Arrays.asList(new Document("teamName", "$seasons.games.homeTeam.name")
                                                .append("goals", "$seasons.games.homeTeam.teamStats.goals")
                                                .append("shotsOnTarget", "$seasons.games.homeTeam.teamStats.shotsOnTarget")
                                                .append("xGoals", "$seasons.games.homeTeam.teamStats.xGoals"),
                                        new Document("teamName", "$seasons.games.awayTeam.name")
                                                .append("goals", "$seasons.games.awayTeam.teamStats.goals")
                                                .append("shotsOnTarget", "$seasons.games.awayTeam.teamStats.shotsOnTarget")
                                                .append("xGoals", "$seasons.games.awayTeam.teamStats.xGoals")))),
                new Document("$unwind", "$game"),
                new Document("$group",
                        new Document("_id",
                                new Document("leagueName", "$leagueName")
                                        .append("season", "$season")
                                        .append("teamName", "$game.teamName"))
                                .append("totalGoals",
                                        new Document("$sum", "$game.goals"))
                                .append("totalShotsOnTarget",
                                        new Document("$sum", "$game.shotsOnTarget"))
                                .append("totalXGoals",
                                        new Document("$sum", "$game.xGoals"))),
                new Document("$project",
                        new Document("_id", 0L)
                                .append("leagueName", "$_id.leagueName")
                                .append("season", "$_id.season")
                                .append("teamName", "$_id.teamName")
                                .append("totalGoals", 1L)
                                .append("totalShotsOnTarget", 1L)
                                .append("totalXGoals", 1L)),
                new Document("$sort",
                        new Document("leagueName", 1L)
                                .append("season", -1L)
                                .append("totalGoals", -1L)));

        // Execute the aggregation pipeline
        AggregateIterable<Document> result = collection.aggregate(pipeline);

        List<TeamGoalsPerLeagueAndSeason> teamGoalsPerLeagueAndSeasons = new ArrayList<>();
        for (Document doc : result) {
            TeamGoalsPerLeagueAndSeason teamGoalsPerLeagueAndSeason = new TeamGoalsPerLeagueAndSeason();
            // Manual mapping from Document to GameStats
            teamGoalsPerLeagueAndSeason.setTeamName(doc.getString("teamName"));
            teamGoalsPerLeagueAndSeason.setLeagueName(doc.getString("leagueName"));
            teamGoalsPerLeagueAndSeason.setSeason(doc.getInteger("season"));
            teamGoalsPerLeagueAndSeason.setTotalGoals(doc.getInteger("totalGoals"));
            teamGoalsPerLeagueAndSeason.setTotalShotsOnTarget(doc.getInteger("totalShotsOnTarget"));
            teamGoalsPerLeagueAndSeason.setTotalXGoals(doc.getDouble("totalXGoals"));

            teamGoalsPerLeagueAndSeasons.add(teamGoalsPerLeagueAndSeason);
        }

        return teamGoalsPerLeagueAndSeasons;
    }

    public void updateTeamStats(TeamStatMongo newStats) {
        int seasonYear = newStats.getSeason();
        int gameID = newStats.getGameID();
        var leagueID = newStats.getLeagueId();

        MongoCollection<Document> collection = database.getCollection("stats");

        // Perform the update
        collection.updateOne(filters(leagueID, seasonYear, gameID), update(newStats, collection), new UpdateOptions().upsert(true).arrayFilters(arrayFilters(seasonYear, gameID)));
    }

    public MongoBulkStats updateTeamStatsBatch(List<TeamStatMongo> teamStatsList) {
        MongoCollection<Document> collection = database.getCollection("stats");
        List<UpdateOneModel<Document>> updates = new ArrayList<>();

        for (TeamStatMongo teamStat : teamStatsList) {
            int leagueID = teamStat.getLeagueId();
            int seasonYear = teamStat.getSeason();
            int gameID = teamStat.getGameID();

            updates.add(new UpdateOneModel<>(filters(leagueID, seasonYear, gameID), update(teamStat, collection), new UpdateOptions().upsert(true).arrayFilters(arrayFilters(seasonYear, gameID))));
        }

        var options = new BulkWriteOptions().ordered(false); // Set to false to continue processing writes if one fails
        var result = collection.bulkWrite(updates, options);
        return new MongoBulkStats(result.getInsertedCount(), result.getMatchedCount(), result.getDeletedCount());
    }


    private Bson update(TeamStatMongo newStats, MongoCollection<Document> collection) {
        var teamStatPath = teamStatPath(newStats, collection);
        return combine(
                set(teamStatPath + ".date", newStats.getDate()),
                set(teamStatPath + ".location", newStats.getLocation()),
                set(teamStatPath + ".goals", newStats.getGoals()),
                set(teamStatPath + ".xGoals", newStats.getExpectedGoals()),
                set(teamStatPath + ".shots", newStats.getShots()),
                set(teamStatPath + ".shotsOnTarget", newStats.getShotsOnTarget()),
                set(teamStatPath + ".deep", newStats.getDeep()),
                set(teamStatPath + ".ppda", newStats.getPpda()),
                set(teamStatPath + ".fouls", newStats.getFouls()),
                set(teamStatPath + ".corners", newStats.getCorners()),
                set(teamStatPath + ".yellowCards", newStats.getYellowCards()),
                set(teamStatPath + ".redCards", newStats.getRedCards()),
                set(teamStatPath + ".result", newStats.getResult())
        );
    }

    private String teamStatPath(TeamStatMongo teamStat, MongoCollection<Document> collection) {
        return "seasons.$[s].games.$[g]." + (isHomeTeam(teamStat, collection) ? "homeTeam" : "awayTeam") + ".teamStats";
    }

    private boolean isHomeTeam(TeamStatMongo newStats,  MongoCollection<Document> collection) {
        // Aggregation pipeline to determine if the team is home or away
        var pipeline = Arrays.asList(
                match(and(eq("leagueID", newStats.getLeagueId()), eq("seasons.season", newStats.getSeason()))),
                unwind("$seasons"),
                unwind("$seasons.games"),
                match(eq("seasons.games.gameID", newStats.getGameID())),
                addFields(new Field<>("isHomeTeam", new Document("$eq", Arrays.asList("$seasons.games.homeTeam.teamID", newStats.getTeamID()))))
        );

        // Execute the aggregation pipeline
        var cursor = collection.aggregate(pipeline).iterator();
        if (cursor.hasNext()) {
            Document doc = cursor.next();
            return doc.getBoolean("isHomeTeam", false);
        }
        throw new RuntimeException("Insufficient data in games");
    }

    private Bson filters(int leagueID, int seasonYear, int gameID) {
        return Filters.and(
                Filters.eq("leagueID", leagueID),
                Filters.eq("seasons.season", seasonYear),
                Filters.eq("seasons.games.gameID", gameID));
    }

    private List<Bson> arrayFilters(int seasonYear, int gameID) {
        return List.of(
                Filters.eq("s.season", seasonYear),
                Filters.eq("g.gameID", gameID)
        );
    }
}
