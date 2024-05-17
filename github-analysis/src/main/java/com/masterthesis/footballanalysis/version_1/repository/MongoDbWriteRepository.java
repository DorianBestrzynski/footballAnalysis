package com.masterthesis.footballanalysis.version_1.repository;

import com.masterthesis.footballanalysis.version_1.dto.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

@Repository("MongoDbWriteRepositoryV1")
@RequiredArgsConstructor
public class MongoDbWriteRepository {
    private final MongoDatabase database;


    public void createFullUser(FullUserDTO user) {
        MongoCollection<Document> collection = database.getCollection("GithubData");

        List<Document> followerDocs = new ArrayList<>();
        if (user.getFollowerList() != null) {
            for (Integer followerId : user.getFollowerList()) {
                followerDocs.add(new Document("follower_id", followerId));
            }
        }

        List<Document> followingDocs = new ArrayList<>();
        if (user.getFollowingList() != null) {
            for (Integer followingId : user.getFollowingList()) {
                followingDocs.add(new Document("following_id", followingId));
            }
        }

        List<Document> repoDocs = new ArrayList<>();
        if (user.getRepoList() != null) {
            for (FullUserDTO.RepositoryDTO repo : user.getRepoList()) {
                repoDocs.add(new Document("repo_id", repo.getRepoId())
                        .append("name", repo.getName())
                        .append("description", repo.getDescription())
                        .append("language", repo.getLanguage())
                        .append("has_wiki", repo.getHasWiki())
                        .append("created_at", repo.getCreatedAt())
                        .append("updated_at", repo.getUpdatedAt())
                        .append("pushed_at", repo.getPushedAt())
                        .append("default_branch", repo.getDefaultBranch())
                        .append("stargazers_count", repo.getStargazersCount())
                        .append("open_issues", repo.getOpenIssues())
                        .append("owner_id", repo.getOwnerId())
                        .append("license", repo.getLicense())
                        .append("size", repo.getSize())
                        .append("fork", repo.getFork()));
            }
        }

        List<Document> commitDocs = new ArrayList<>();
        if (user.getCommitList() != null) {
            for (FullUserDTO.CommitDTO commit : user.getCommitList()) {
                commitDocs.add(new Document("commit_id", commit.getCommitId())
                        .append("message", commit.getMessage())
                        .append("commit_at", commit.getCommitAt())
                        .append("generate_at", commit.getGenerateAt())
                        .append("repo_id", commit.getRepoId())
                        .append("author_id", commit.getAuthorId())
                        .append("committer_id", commit.getCommitterId())
                        .append("repo_name", commit.getRepoName())
                        .append("repo_description", commit.getRepoDescription()));
            }
        }

        Document userDoc = new Document("user_id", user.getUserId())
                .append("name", user.getName())
                .append("type", user.getType())
                .append("bio", user.getBio())
                .append("email", user.getEmail())
                .append("login", user.getLogin())
                .append("company", user.getCompany())
                .append("blog", user.getBlog())
                .append("location", user.getLocation())
                .append("created_at", user.getCreatedAt())
                .append("updated_at", user.getUpdatedAt())
                .append("hirable", user.getHirable())
                .append("is_suspicious", user.getIsSuspicious())
                .append("follower_list", followerDocs)
                .append("following_list", followingDocs)
                .append("repo_list", repoDocs)
                .append("commit_list", commitDocs);

        collection.insertOne(userDoc);
    }


    public void addCommitToUser(int userId, WriteCommitDTO commit) {
        MongoCollection<Document> collection = database.getCollection("GithubData");

        Document commitDoc = new Document("commit_id", commit.getCommitId())
                .append("message", commit.getMessage())
                .append("commit_at", commit.getCommitAt())
                .append("generate_at", commit.getGenerateAt())
                .append("repo_id", commit.getRepoId())
                .append("author_id", commit.getAuthorId())
                .append("committer_id", commit.getCommitterId())
                .append("repo_name", commit.getRepoName())
                .append("repo_description", commit.getRepoDescription());

        collection.updateOne(
                new Document("user_id", userId),
                new Document("$push", new Document("commit_list", commitDoc))
        );
    }

    public void addCommitsToUser(int userId, List<WriteCommitDTO> commits) {
        MongoCollection<Document> collection = database.getCollection("GithubData");

        List<Document> commitDocs = new ArrayList<>();
        for (WriteCommitDTO commit : commits) {
            Document commitDoc = new Document("commit_id", commit.getCommitId())
                    .append("message", commit.getMessage())
                    .append("commit_at", commit.getCommitAt())
                    .append("generate_at", commit.getGenerateAt())
                    .append("repo_id", commit.getRepoId())
                    .append("author_id", commit.getAuthorId())
                    .append("committer_id", commit.getCommitterId())
                    .append("repo_name", commit.getRepoName())
                    .append("repo_description", commit.getRepoDescription());
            commitDocs.add(commitDoc);
        }

        collection.updateOne(
                new Document("user_id", userId),
                new Document("$push", new Document("commit_list", new Document("$each", commitDocs)))
        );
    }

    public void createGitUser(GitUser user) {
        MongoCollection<Document> collection = database.getCollection("GithubData");

        Document doc = new Document("user_id", user.getUserId())
                .append("name", user.getName())
                .append("type", user.getType())
                .append("bio", user.getBio())
                .append("email", user.getEmail())
                .append("login", user.getLogin())
                .append("company", user.getCompany())
                .append("blog", user.getBlog())
                .append("location", user.getLocation())
                .append("created_at", user.getCreatedAt())
                .append("updated_at", user.getUpdatedAt())
                .append("hirable", user.getHirable())
                .append("is_suspicious", user.getIsSuspicious());

        collection.insertOne(doc);
    }
}
