package com.masterthesis.footballanalysis.player.dto;

import lombok.Data;

@Data
public class TopScorers {
    private String leagueName;
    private String playerName;
    private Long totalGoals;
    private Long totalAssists;
}
