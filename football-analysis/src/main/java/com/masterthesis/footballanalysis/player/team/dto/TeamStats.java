package com.masterthesis.footballanalysis.player.team.dto;

import lombok.Data;

@Data
public class TeamStats {
    private String leagueName;
    private String teamName;
    private double avgGoals;
    private double avgShots;
}
