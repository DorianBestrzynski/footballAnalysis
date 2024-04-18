package com.masterthesis.footballanalysis.version_1.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Query1DTOMongo {
    private int gameId;
    private String leagueName;
    private int season;
    private String date;
    private int homeGoals;
    private int awayGoals;
}
