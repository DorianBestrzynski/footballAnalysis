package com.masterthesis.footballanalysis.version_1_2.dto;

import lombok.Data;

import java.util.List;

@Data
public class Player {
    Integer playerId;
    String name;
    List<PlayerAppearance> appearances;
    List<PlayerShots> shots;
}
