package com.masterthesis.footballanalysis.version_1.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FullUserDTO {
    private Integer userId;
    private String name;
    private String type;
    private String bio;
    private String email;
    private String login;
    private String company;
    private String blog;
    private String location;
    private Date createdAt;
    private Date updatedAt;
    private Boolean hirable;
    private Boolean isSuspicious;
    private List<Integer> followerList;
    private List<Integer> followingList;
    private List<RepositoryDTO> repoList;
    private List<CommitDTO> commitList;

    // Nested DTO for Repository
    @Data
    public static class RepositoryDTO {
        private Integer repoId;
        private String name;
        private String description;
        private String language;
        private Boolean hasWiki;
        private Date createdAt;
        private Date updatedAt;
        private Date pushedAt;
        private String defaultBranch;
        private Integer stargazersCount;
        private Integer openIssues;
        private Integer ownerId;
        private String license;
        private Integer size;
        private Boolean fork;
    }

    // Nested DTO for Commit
    @Data
    public static class CommitDTO {
        private Integer commitId;
        private String message;
        private Date commitAt;
        private Date generateAt;
        private Integer repoId;
        private Integer authorId;
        private Integer committerId;
        private String repoName;
        private String repoDescription;
    }
}

