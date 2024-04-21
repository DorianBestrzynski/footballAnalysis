package com.masterthesis.footballanalysis.version_2.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Query4DTOPostgres {
    private Timestamp date;
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
