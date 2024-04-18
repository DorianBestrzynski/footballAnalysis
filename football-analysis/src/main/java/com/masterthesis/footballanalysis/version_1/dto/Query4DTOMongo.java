package com.masterthesis.footballanalysis.version_1.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Query4DTOMongo {
    private String leagueName;
    private String match;
    private String date;
    private int homeShots;
}
