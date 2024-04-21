package com.masterthesis.footballanalysis.version_2.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Query5DTOPostgres {
    private int gameId;
    private Timestamp date;
    private int season;
    private int totalShots;
    private int goalsScored;
}
