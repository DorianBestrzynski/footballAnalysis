package com.masterthesis.footballanalysis.version_2.dto;

import lombok.Data;

@Data
public class Query4DTOMongo {
    private String date;
    private int season;
    private String match;
    private String leagueName;
    private String result;
    private int minute;
    private String situation;
    private String shotResult;
    private String playerName;
    private int playerGoals;
    private int playerShots;
}
