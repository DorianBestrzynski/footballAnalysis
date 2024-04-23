package com.masterthesis.footballanalysis.version_1_2.dto;

import lombok.Data;

@Data
public class Query3DTOPostgres {
    private Long playerId;
    private String name;
    private int minute;
    private String situation;
    private String lastAction;
    private String shotType;
    private String shotResult;
}
