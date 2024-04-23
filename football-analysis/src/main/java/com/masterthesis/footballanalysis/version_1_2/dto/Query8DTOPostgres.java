package com.masterthesis.footballanalysis.version_1_2.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Query8DTOPostgres {
    private String leagueName;
    private String matchName;
    private Timestamp date;
    private Long shots;
}
