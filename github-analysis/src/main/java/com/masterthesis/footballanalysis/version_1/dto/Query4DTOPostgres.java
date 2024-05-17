package com.masterthesis.footballanalysis.version_1.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Query4DTOPostgres {
    private int userId;
    private String userName;
    private int followerId;
    private int followingId;
    private int repoId;
    private String repoName;
    private int commitId;
    private String commitMessage;
}
