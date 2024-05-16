package com.masterthesis.footballanalysis.version_1.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Query1DTOPostgres {
    private int userId;
    private String name;
    private String login;
}
