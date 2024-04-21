package com.masterthesis.footballanalysis.version_2.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class GameDocument {
    private int gameId;
    private Date date;
    private String leagueName;
    private int season;
    private int homeGoals;
    private int awayGoals;
    private float homeProbability;
    private float awayProbability;
    private float drawProbability;
    private int homeGoalsHalfTime;
    private int awayGoalsHalfTime;
    private TeamInfo homeTeam;
    private TeamInfo awayTeam;
    private List<PlayerAppearance> appearances;
    private List<Shot> shots;

    @Data
    public static class TeamInfo {
        private int teamID;
        private String name;
        private TeamStats teamStats;
    }

    @Data
    public static class TeamStats {
        private long teamstatId;
        private char location;
        private int goals;
        private float xGoals;
        private int shots;
        private int shotsOnTarget;
        private int deep;
        private float ppda;
        private int fouls;
        private int corners;
        private int yellowCards;
        private int redCards;
        private char result;
    }

    @Data
    public static class PlayerAppearance {
        private long appearanceId;
        private int gameID;
        private int playerID;
        private int goals;
        private int ownGoals;
        private int shots;
        private float xGoals;
        private float xGoalsChain;
        private float xGoalsBuildup;
        private int assists;
        private int keyPasses;
        private float xAssists;
        private String position;
        private int positionOrder;
        private int yellowCard;
        private int redCard;
        private int time;
        private String substituteIn;
        private String substituteOut;
        private int leagueId;
        private String playerName;
        private String leagueName;
        private String understatNotation;
    }

    @Data
    public static class Shot {
        private long shotId;
        private int gameId;
        private int shooterId;
        private Integer assisterId;
        private int minute;
        private String situation;
        private String lastAction;
        private String shotType;
        private String shotResult;
        private float xGoal;
        private float positionX;
        private float positionY;
        private String shooterName;
        private String assisterName;
    }
}
