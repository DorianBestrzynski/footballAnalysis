package com.masterthesis.footballanalysis.player.team.dto;

import lombok.Data;

@Data
public class TeamGoalsPerLeagueAndSeason {
    String teamName;
    String leagueName;
    Integer season;
    Integer totalGoals;
    Integer totalShotsOnTarget;
    Double totalXGoals;
    Double avgShotsOnTargetPerGoal;
}
