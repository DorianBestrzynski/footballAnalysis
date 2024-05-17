package com.masterthesis.footballanalysis.version_1.dto;

import lombok.Data;

import java.sql.Timestamp;


@Data
public class WriteCommitDTO {
    private Integer commitId;
    private String message;
    private Timestamp commitAt;
    private Timestamp generateAt;
    private Integer repoId;
    private Integer authorId;
    private Integer committerId;
    private String repoName;
    private String repoDescription;
}
