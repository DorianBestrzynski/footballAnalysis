package com.masterthesis.footballanalysis.version_2.dto;

import lombok.Data;

@Data
public class Query3DTOMongo {
    private String date;
    private int season;
    private int minute;
    private String situation;
    private String shotType;
    private String shotResult;
}
