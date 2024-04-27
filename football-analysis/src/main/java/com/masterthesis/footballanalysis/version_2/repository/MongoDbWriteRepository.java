package com.masterthesis.footballanalysis.version_2.repository;

import com.masterthesis.footballanalysis.version_2.dto.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

@Repository("MongoDbWriteRepositoryV2")
@RequiredArgsConstructor
public class MongoDbWriteRepository {
    private final MongoDatabase database;

    public void updateTable(GameDocument gameDocument) {
        MongoCollection<Document> collection = database.getCollection("games");
        collection.insertOne(gameDocumentToMongoDocument(gameDocument));
    }

    private Document gameDocumentToMongoDocument(GameDocument gameDocument) {
        return new Document()
                .append("gameID", gameDocument.getGameId())
                .append("date", gameDocument.getDate())
                .append("leagueName", gameDocument.getLeagueName())
                .append("season", gameDocument.getSeason())
                .append("homeGoals", gameDocument.getHomeGoals())
                .append("awayGoals", gameDocument.getAwayGoals())
                .append("homeProbability", gameDocument.getHomeProbability())
                .append("awayProbability", gameDocument.getAwayProbability())
                .append("drawProbability", gameDocument.getDrawProbability())
                .append("homeGoalsHalfTime", gameDocument.getHomeGoalsHalfTime())
                .append("awayGoalsHalfTime", gameDocument.getAwayGoalsHalfTime())
                .append("homeTeam", teamInfoToDocument(gameDocument.getHomeTeam()))
                .append("awayTeam", teamInfoToDocument(gameDocument.getAwayTeam()))
                .append("appearances", appearancesToListDocuments(gameDocument.getAppearances()))
                .append("shots", shotsToListDocuments(gameDocument.getShots()));
    }

    private Document teamInfoToDocument(GameDocument.TeamInfo teamInfo) {
        return new Document()
                .append("teamID", teamInfo.getTeamID())
                .append("name", teamInfo.getName())
                .append("teamStats", new Document()
                        .append("teamstatId", teamInfo.getTeamStats().getTeamstatId())
                        .append("location", String.valueOf(teamInfo.getTeamStats().getLocation()))
                        .append("goals", teamInfo.getTeamStats().getGoals())
                        .append("xGoals", teamInfo.getTeamStats().getXGoals())
                        .append("shots", teamInfo.getTeamStats().getShots())
                        .append("shotsOnTarget", teamInfo.getTeamStats().getShotsOnTarget())
                        .append("deep", teamInfo.getTeamStats().getDeep())
                        .append("ppda", teamInfo.getTeamStats().getPpda())
                        .append("fouls", teamInfo.getTeamStats().getFouls())
                        .append("corners", teamInfo.getTeamStats().getCorners())
                        .append("yellowCards", teamInfo.getTeamStats().getYellowCards())
                        .append("redCards", teamInfo.getTeamStats().getRedCards())
                        .append("result", String.valueOf(teamInfo.getTeamStats().getResult())));
    }

    private List<Document> appearancesToListDocuments(List<GameDocument.PlayerAppearance> appearances) {
        return appearances.stream()
                .map(appearance -> new Document()
                        .append("appearanceId", appearance.getAppearanceId())
                        .append("gameID", appearance.getGameID())
                        .append("playerID", appearance.getPlayerID())
                        .append("goals", appearance.getGoals())
                        .append("ownGoals", appearance.getOwnGoals())
                        .append("shots", appearance.getShots())
                        .append("xGoals", appearance.getXGoals())
                        .append("xGoalsChain", appearance.getXGoalsChain())
                        .append("xGoalsBuildup", appearance.getXGoalsBuildup())
                        .append("assists", appearance.getAssists())
                        .append("keyPasses", appearance.getKeyPasses())
                        .append("xAssists", appearance.getXAssists())
                        .append("position", appearance.getPosition())
                        .append("positionOrder", appearance.getPositionOrder())
                        .append("yellowCard", appearance.getYellowCard())
                        .append("redCard", appearance.getRedCard())
                        .append("time", appearance.getTime())
                        .append("substituteIn", appearance.getSubstituteIn())
                        .append("substituteOut", appearance.getSubstituteOut())
                        .append("leagueId", appearance.getLeagueId())
                        .append("playerName", appearance.getPlayerName())
                        .append("leagueName", appearance.getLeagueName())
                        .append("understatNotation", appearance.getUnderstatNotation()))
                .collect(Collectors.toList());
    }

    private List<Document> shotsToListDocuments(List<GameDocument.Shot> shots) {
        return shots.stream()
                .map(shot -> new Document()
                        .append("shotId", shot.getShotId())
                        .append("gameID", shot.getGameId())
                        .append("shooterId", shot.getShooterId())
                        .append("assisterId", shot.getAssisterId())
                        .append("minute", shot.getMinute())
                        .append("situation", shot.getSituation())
                        .append("lastAction", shot.getLastAction())
                        .append("shotType", shot.getShotType())
                        .append("shotResult", shot.getShotResult())
                        .append("xGoal", shot.getXGoal())
                        .append("positionX", shot.getPositionX())
                        .append("positionY", shot.getPositionY())
                        .append("shooterName", shot.getShooterName())
                        .append("assisterName", shot.getAssisterName()))
                .collect(Collectors.toList());
    }

    public void updateTeamStats(TeamStatMongo newStats) {
        int gameID = newStats.getGameID();

        MongoCollection<Document> collection = database.getCollection("Statistics");

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
        MongoCollection<Document> collection = database.getCollection("Statistics");
        collection.insertOne(game(game));
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
