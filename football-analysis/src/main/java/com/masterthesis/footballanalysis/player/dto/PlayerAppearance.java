package com.masterthesis.footballanalysis.player.dto;

import lombok.Data;

@Data
public class PlayerAppearance {
    private int gameID;
    private int leagueID;
    private int time;
    private int goals;
    private int ownGoals;
    private double xGoals;
    private int assists;
    private String position;
    private int positionOrder;
    private double xAssists;
    private int shots;
    private int keyPasses;
    private int yellowCards;
    private int redCards;
    private double xGoalsChain;
    private double xGoalsBuildup;

}
