package com.masterthesis.footballanalysis.version_2.dto;

import lombok.Data;

@Data
public class PlayerAppearance {
    private int goals;
    private int ownGoals;
    private int shots;
    private float xGoals;
    private float xGoalsChain;
    private float xGoalsBuildup;
    private int assists;
    private int keyPasses;
    private float xAssists;
    private String position;
    private int positionOrder;
    private int yellowCard;
    private int redCard;
    private int time;
    private String substituteIn;
    private String substituteOut;
}
