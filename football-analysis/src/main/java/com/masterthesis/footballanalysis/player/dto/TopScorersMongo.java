package com.masterthesis.footballanalysis.player.dto;

import lombok.Data;

@Data
public class TopScorersMongo {
    private Integer season;
    private String leagueName;
    private String playerName;
    private Integer totalGoals;
    private Integer totalAssists;
}
