package com.masterthesis.footballanalysis.version_1_2.dto;

import lombok.Data;

@Data
public class Query1DTOMongo {
    private int gameId;
    private String leagueName;
    private int season;
    private String date;
    private int homeGoals;
    private int awayGoals;
}
