package com.masterthesis.footballanalysis.player.team.dto;

import lombok.Data;

import java.util.Date;

@Data
public class GameStatsMongo {
    private String leagueName;
    private String matchName;
    private String date;
    private Integer shots;
}
