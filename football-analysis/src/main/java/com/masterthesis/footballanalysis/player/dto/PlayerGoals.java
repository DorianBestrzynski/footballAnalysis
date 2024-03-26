package com.masterthesis.footballanalysis.player.dto;

import lombok.Data;

@Data
public class PlayerGoals {
    private String playerName;
    private int gameId;
    private String teamName;
    private int goals;
}
