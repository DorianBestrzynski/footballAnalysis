package com.masterthesis.footballanalysis.version_2.dto;

import lombok.Data;

@Data
public class Write1DTO {
    private League league;
    private Player player;
    private Team team1;
    private Team team2;
    private Game game;
    private PlayerAppearance playerAppearance;
    private TeamStat teamStat1;
    private TeamStat teamStat2;
    private Shot shot;
}
