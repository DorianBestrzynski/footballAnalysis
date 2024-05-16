package com.masterthesis.footballanalysis.version_1.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class TeamStatMongo {
    private Integer gameID;
    private Integer teamID;
    private Timestamp date;
    private Character location;
    private Integer goals;
    private Float expectedGoals;
    private Integer shots;
    private Integer shotsOnTarget;
    private Integer deep;
    private Float ppda;
    private Integer fouls;
    private Integer corners;
    private Integer yellowCards;
    private Integer redCards;
    private Character result;
}
