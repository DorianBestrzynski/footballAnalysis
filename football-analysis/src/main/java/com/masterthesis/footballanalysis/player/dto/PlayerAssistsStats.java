package com.masterthesis.footballanalysis.player.dto;

import lombok.Data;

@Data
public class PlayerAssistsStats {
    private String playerName;
    private int totalAssists;
    private Long totalGames;
    private double avgAssistsPerGame;
}
