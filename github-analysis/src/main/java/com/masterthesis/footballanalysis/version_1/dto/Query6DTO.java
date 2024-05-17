package com.masterthesis.footballanalysis.version_1.dto;

import lombok.Data;

@Data
public class Query6DTO {
    private int userId;
    private String name;
    private int repoId;
    private String repoName;
    private String commitDate;
    private int commitCount;
}
