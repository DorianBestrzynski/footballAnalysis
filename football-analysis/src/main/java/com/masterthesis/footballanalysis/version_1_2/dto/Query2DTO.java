package com.masterthesis.footballanalysis.version_1_2.dto;

import lombok.Data;

@Data
public class Query2DTO {
    private String location;
    private int goals;
    private int shots;
    private Double xGoals;
    private int shotsontarget;
    private int deep;
}
