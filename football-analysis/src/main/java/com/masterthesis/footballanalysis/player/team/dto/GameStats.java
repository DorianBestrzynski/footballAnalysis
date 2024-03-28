package com.masterthesis.footballanalysis.player.team.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.time.Instant;

@Data
public class GameStats {
    private String leagueName;
    private String matchName;
    private Timestamp date;
    private Long shots;
}
