package com.masterthesis.footballanalysis.version_1.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Query1DTOPostgres {
    private int gameId;
    private int leagueId;
    private int season;
    private Timestamp date;
    private int homeGoals;
    private int awayGoals;
}
