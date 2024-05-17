package com.masterthesis.footballanalysis.version_1.dto;

import lombok.Data;

import java.util.List;

@Data
public class Query4v2DTO {
    private int userId;
    private String userName;
    private int repoId;
    private String repoName;
    private int commitId;
    private String commitMessage;
    private List<CommitDTO> commits;
}
