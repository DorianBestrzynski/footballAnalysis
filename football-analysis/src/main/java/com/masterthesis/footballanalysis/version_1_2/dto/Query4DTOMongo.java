package com.masterthesis.footballanalysis.version_1_2.dto;

import lombok.Data;

@Data
public class Query4DTOMongo {
    private String leagueName;
    private String match;
    private String date;
    private int homeShots;
}
