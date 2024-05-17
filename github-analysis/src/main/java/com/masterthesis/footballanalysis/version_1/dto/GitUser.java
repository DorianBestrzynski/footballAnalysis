package com.masterthesis.footballanalysis.version_1.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class GitUser {
    private Integer userId;
    private String name;
    private String type;
    private String bio;
    private String email;
    private String login;
    private String company;
    private String blog;
    private String location;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Boolean hirable;
    private Boolean isSuspicious;
}
