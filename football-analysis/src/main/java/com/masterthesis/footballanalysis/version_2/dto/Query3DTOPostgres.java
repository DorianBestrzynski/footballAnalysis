package com.masterthesis.footballanalysis.version_2.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Query3DTOPostgres {
    private Timestamp date;
    private int season;
    private int minute;
    private String situation;
    private String shotType;
    private String shotResult;
}
