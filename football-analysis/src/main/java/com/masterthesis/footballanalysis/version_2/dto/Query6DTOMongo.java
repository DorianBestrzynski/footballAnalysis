package com.masterthesis.footballanalysis.version_2.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Query6DTOMongo {
    private String date;
    private int season;
    private String match;
    private String leagueName;
    private String result;
    private double avgShotsPerPlayer;
    private long totalShots;
    private long totalGoals;
    private int minute;
    private String situation;
    private String shotResult;
    private String playerName;
    private int playerGoals;
    private int playerShots;
    private long startingPlayers;
    private long substitutedPlayers;
}
