package com.masterthesis.footballanalysis.version_1_2.dto;

import lombok.Data;

@Data
public class PlayerShots {
    private int gameID;
    private int shooterID;
    private int assisterID;
    private int minute;
    private String situation;
    private String lastAction;
    private String shotType;
    private String shotResult;
    private double xGoal;
    private double positionX;
    private double positionY;
}
