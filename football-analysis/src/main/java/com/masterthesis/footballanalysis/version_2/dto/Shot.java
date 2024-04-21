package com.masterthesis.footballanalysis.version_2.dto;

import lombok.Data;

@Data
public class Shot {
    private int gameId;
    private int shooterId;
    private int assisterId;
    private int minute;
    private String situation;
    private String lastAction;
    private String shotType;
    private String shotResult;
    private float xGoal;
    private float positionX;
    private float positionY;
}
