package com.masterthesis.footballanalysis.version_2.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Query5DTOMongo {
    private int gameId;
    private String date;
    private int season;
    private long totalShots;
    private long goalsScored;
}
