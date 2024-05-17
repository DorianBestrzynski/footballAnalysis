package com.masterthesis.footballanalysis.version_1.repository;

import com.masterthesis.footballanalysis.version_1.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository("PostgresWriteRepositoryV1")
@RequiredArgsConstructor
public class PostgresWriteRepository {
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void createFullUser(FullUserDTO user) {
        // Insert into git_user table
        String userSql = "INSERT INTO git_user (name, type, bio, email, login, company, blog, location, created_at, updated_at, hirable, is_suspicious) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder userKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(userSql, new String[] {"user_id"});
            ps.setString(1, user.getName());
            ps.setString(2, user.getType());
            ps.setString(3, user.getBio());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getLogin());
            ps.setString(6, user.getCompany());
            ps.setString(7, user.getBlog());
            ps.setString(8, user.getLocation());
            ps.setTimestamp(9, new Timestamp(user.getCreatedAt().getTime()));
            ps.setTimestamp(10, new Timestamp(user.getUpdatedAt().getTime()));
            ps.setBoolean(11, user.getHirable());
            ps.setBoolean(12, user.getIsSuspicious());
            return ps;
        }, userKeyHolder);

        // Retrieve the generated user ID
        int userId = userKeyHolder.getKey().intValue();

        // Insert into Followers table
        String followerSql = "INSERT INTO Followers (user_id, follower_id) VALUES (?, ?)";
        List<Object[]> followerBatchArgs = user.getFollowerList().stream()
                .map(followerId -> new Object[]{userId, followerId})
                .collect(Collectors.toList());
        jdbcTemplate.batchUpdate(followerSql, followerBatchArgs);

        // Insert into Following table
        String followingSql = "INSERT INTO Following (user_id, following_id) VALUES (?, ?)";
        List<Object[]> followingBatchArgs = user.getFollowingList().stream()
                .map(followingId -> new Object[]{userId, followingId})
                .collect(Collectors.toList());
        jdbcTemplate.batchUpdate(followingSql, followingBatchArgs);

        // Insert into Repository table
        String repoSql = "INSERT INTO Repository (name, description, language, has_wiki, created_at, updated_at, pushed_at, default_branch, stargazers_count, open_issues, owner_id, license, size, fork) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        List<Integer> repoIds = new ArrayList<>();
        for (FullUserDTO.RepositoryDTO repo : user.getRepoList()) {
            KeyHolder repoKeyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(repoSql, new String[] {"repo_id"});
                ps.setString(1, repo.getName());
                ps.setString(2, repo.getDescription());
                ps.setString(3, repo.getLanguage());
                ps.setBoolean(4, repo.getHasWiki());
                ps.setTimestamp(5, new Timestamp(repo.getCreatedAt().getTime()));
                ps.setTimestamp(6, new Timestamp(repo.getUpdatedAt().getTime()));
                ps.setTimestamp(7, new Timestamp(repo.getPushedAt().getTime()));
                ps.setString(8, repo.getDefaultBranch());
                ps.setInt(9, repo.getStargazersCount());
                ps.setInt(10, repo.getOpenIssues());
                ps.setInt(11, userId); // owner_id is the generated user ID
                ps.setString(12, repo.getLicense());
                ps.setInt(13, repo.getSize());
                ps.setBoolean(14, repo.getFork());
                return ps;
            }, repoKeyHolder);

            // Retrieve the generated repository ID
            int repoId = repoKeyHolder.getKey().intValue();
            repoIds.add(repoId);

            // Insert into Commits table
            String commitSql = "INSERT INTO Commits (message, commit_at, generate_at, repo_id, author_id, committer_id, repo_name, repo_description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            List<Object[]> commitBatchArgs = user.getCommitList().stream()
                    .filter(commit -> commit.getRepoId().equals(repo.getRepoId()))
                    .map(commit -> new Object[]{
                            commit.getMessage(), new Timestamp(commit.getCommitAt().getTime()), new Timestamp(commit.getGenerateAt().getTime()),
                            repoId, userId, commit.getCommitterId(), commit.getRepoName(), commit.getRepoDescription()
                    })
                    .collect(Collectors.toList());
            jdbcTemplate.batchUpdate(commitSql, commitBatchArgs);
        }

        // Insert into UserRepositories table
        String userRepoSql = "INSERT INTO UserRepositories (user_id, repo_id) VALUES (?, ?)";
        List<Object[]> userRepoBatchArgs = repoIds.stream()
                .map(repoId -> new Object[]{userId, repoId})
                .collect(Collectors.toList());
        jdbcTemplate.batchUpdate(userRepoSql, userRepoBatchArgs);
    }

    public void createCommit(WriteCommitDTO commit) {
        String sql = "INSERT INTO Commits (message, commit_at, generate_at, repo_id, author_id, committer_id, repo_name, repo_description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                commit.getMessage(),
                commit.getCommitAt(),
                commit.getGenerateAt(),
                commit.getRepoId(),
                commit.getAuthorId(),
                commit.getCommitterId(),
                commit.getRepoName(),
                commit.getRepoDescription()
        );
    }

    public void createCommits(List<WriteCommitDTO> commits) {
        String sql = "INSERT INTO Commits (message, commit_at, generate_at, repo_id, author_id, committer_id, repo_name, repo_description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();
        for (WriteCommitDTO commit : commits) {
            batchArgs.add(new Object[]{
                    commit.getMessage(),
                    commit.getCommitAt(),
                    commit.getGenerateAt(),
                    commit.getRepoId(),
                    commit.getAuthorId(),
                    commit.getCommitterId(),
                    commit.getRepoName(),
                    commit.getRepoDescription()
            });
        }

        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    public void createGitUser(GitUser user) {
        String sql = "INSERT INTO git_user (name, type, bio, email, login, company, blog, location, created_at, updated_at, hirable, is_suspicious) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                user.getName(),
                user.getType(),
                user.getBio(),
                user.getEmail(),
                user.getLogin(),
                user.getCompany(),
                user.getBlog(),
                user.getLocation(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getHirable(),
                user.getIsSuspicious()
        );
    }
}
