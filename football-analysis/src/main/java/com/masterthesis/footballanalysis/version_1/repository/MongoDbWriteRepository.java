package com.masterthesis.footballanalysis.version_1.repository;

import com.masterthesis.footballanalysis.version_1.dto.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

@Repository
@RequiredArgsConstructor
public class MongoDbWriteRepository {
    private final MongoDatabase database;


    public void updatePlayer(Player player) {
        MongoCollection<Document> collection = database.getCollection("Player_Appearances_Shots");

        // Insert or update the player document into the collection
        var filter = new Document("playerID", player.getPlayerId());
        var  update = new Document("$set", player(player));
        UpdateOptions options = new UpdateOptions().upsert(true); // This will insert the document if it doesn't exist, or update it if it does

        collection.updateOne(filter, update, options);
    }

    private Document player(Player player) {
        return new Document("playerID", player.getPlayerId())
                .append("name", player.getName())
                .append("appearances", appearances(player))
                .append("shots", shots(player));
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

    private List<Document> shots(Player player) {
        List<Document> shotsDocs = new ArrayList<>();
        for (PlayerShots shot : player.getShots()) {
            Document appearanceDoc = new Document("gameID", shot.getGameID())
                    .append("gameID", shot.getGameID())
                    .append("shooterID", shot.getShooterID())
                    .append("assisterID", shot.getAssisterID())
                    .append("minute", shot.getMinute())
                    .append("situation", shot.getSituation())
                    .append("lastAction", shot.getLastAction())
                    .append("shotType", shot.getShotType())
                    .append("shotResult", shot.getShotResult())
                    .append("xGoal", shot.getXGoal())
                    .append("positionX", shot.getPositionX())
                    .append("positionY", shot.getPositionY());
            shotsDocs.add(appearanceDoc);
        }
        return shotsDocs;
    }

    public void updateTeamStats(TeamStatMongo newStats) {
        int gameID = newStats.getGameID();

        MongoCollection<Document> collection = database.getCollection("Game_Leagues_Teams_TeamStats");

        // Perform the update
        collection.updateOne(filters(gameID), update(newStats, collection));
    }

    private Bson update(TeamStatMongo newStats, MongoCollection<Document> collection) {
        var isHomeTeam = isHomeTeam(newStats.getGameID(), newStats.getTeamID(), collection);
        var teamStatPrefix = isHomeTeam ? "homeTeam.teamStats" : "awayTeam.teamStats";
        var teamStatSuffix = isHomeTeam ? "_home" : "_away";

        return combine(
                set(teamStatPrefix + ".date" + teamStatSuffix, newStats.getDate()),
                set(teamStatPrefix + ".location" + teamStatSuffix, newStats.getLocation()),
                set(teamStatPrefix + ".goals" + teamStatSuffix, newStats.getGoals()),
                set(teamStatPrefix + ".xGoals" + teamStatSuffix, newStats.getExpectedGoals()),
                set(teamStatPrefix + ".shots" + teamStatSuffix, newStats.getShots()),
                set(teamStatPrefix + ".shotsOnTarget" + teamStatSuffix, newStats.getShotsOnTarget()),
                set(teamStatPrefix + ".deep" + teamStatSuffix, newStats.getDeep()),
                set(teamStatPrefix + ".ppda" + teamStatSuffix, newStats.getPpda()),
                set(teamStatPrefix + ".fouls" + teamStatSuffix, newStats.getFouls()),
                set(teamStatPrefix + ".corners"+ teamStatSuffix, newStats.getCorners()),
                set(teamStatPrefix + ".yellowCards"+ teamStatSuffix, newStats.getYellowCards()),
                set(teamStatPrefix + ".redCards" + teamStatSuffix, newStats.getRedCards()),
                set(teamStatPrefix + ".result" + teamStatSuffix, newStats.getResult())
        );
    }

    private boolean isHomeTeam(Integer gameID, Integer teamID, MongoCollection<Document> collection) {
        Bson filter = Filters.eq("gameID", gameID);

        // Fetch the game document
        Document gameDocument = collection.find(filter).first();

        if (gameDocument != null) {
            Document homeTeam = (Document) gameDocument.get("homeTeam");
            if (homeTeam != null) {
                Integer homeTeamID = homeTeam.getInteger("teamID");
                return homeTeamID.equals(teamID);
            }
        }
        return false;
    }

    private Bson filters(int gameID) {
        return Filters.eq("gameID", gameID);
    }

    public void updateGame(Game game) {
        MongoCollection<Document> collection = database.getCollection("Game_Leagues_Teams_TeamStats");

        // Insert or update the player document into the collection
        var filter = new Document("gameID", game.getGameId());
        var  update = new Document("$set", game(game));
        UpdateOptions options = new UpdateOptions().upsert(true); // This will insert the document if it doesn't exist, or update it if it does

        collection.updateOne(filter, update, options);
    }

    private Document game(Game game) {
        return new Document("gameID", game.getGameId())
                .append("date", game.getDate())
                .append("leagueName", game.getLeagueName())
                .append("season", game.getSeason())
                .append("homeGoals", game.getHomeGoals())
                .append("awayGoals", game.getAwayGoals())
                .append("homeProbability", game.getHomeProbability())
                .append("awayProbability", game.getAwayProbability())
                .append("drawProbability", game.getDrawProbability())
                .append("homeGoalsHalfTime", game.getHomeGoalsHalfTime())
                .append("awayGoalsHalfTime", game.getAwayGoalsHalfTime());
    }
}
