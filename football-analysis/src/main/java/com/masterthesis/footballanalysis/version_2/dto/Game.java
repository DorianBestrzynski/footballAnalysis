package com.masterthesis.footballanalysis.version_2.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Game {
    Integer gameId;
    Timestamp date;
    Integer leagueId;
    String leagueName;
    Integer season;
    Integer homeTeamId;
    Integer awayTeamId;
    Integer homeGoals;
    Integer awayGoals;
    double homeProbability;
    double awayProbability;
    double drawProbability;
    Integer homeGoalsHalfTime;
    Integer awayGoalsHalfTime;
}
