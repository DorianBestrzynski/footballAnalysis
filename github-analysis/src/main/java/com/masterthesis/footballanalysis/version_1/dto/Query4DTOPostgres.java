package com.masterthesis.footballanalysis.version_1.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Query4DTOPostgres {
    private String leagueName;
    private String match;
    private Timestamp date;
    private int homeShots;
}
