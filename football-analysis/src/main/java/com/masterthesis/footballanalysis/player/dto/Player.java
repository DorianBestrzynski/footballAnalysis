package com.masterthesis.footballanalysis.player.dto;

import lombok.Data;

import java.util.List;

@Data
public class Player {
    Integer playerId;
    String name;
    List<PlayerAppearance> appearances;
}
