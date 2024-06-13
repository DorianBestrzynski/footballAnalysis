package com.masterthesis.footballanalysis.version_1.repository;

import com.masterthesis.footballanalysis.version_1.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("PostgresReadRepositoryV1")
@RequiredArgsConstructor
public class PostgresReadRepository {
    private final JdbcTemplate jdbcTemplate;

    public List<Query1DTOPostgres> query1() {
        String query = "SELECT g.user_id, g.name, g.login " +
                "FROM git_user g  " +
                "LEFT JOIN followers f ON g.user_id = f.user_id  " +
                "WHERE f.user_id IS NULL " +
                "LIMIT 10000 ";
        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Query1DTOPostgres query1 = new Query1DTOPostgres();
            query1.setUserId(rs.getInt("user_id"));
            query1.setName(rs.getString("name"));
            query1.setLogin(rs.getString("login"));
            return query1;
        });
    }

    public List<Query1DTOPostgres> query1_2() {
        String query = "SELECT g.user_id, g.name, g.login " +
                "FROM git_user g  " +
                "WHERE g.user_id=763 ";

        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Query1DTOPostgres query1 = new Query1DTOPostgres();
            query1.setUserId(rs.getInt("user_id"));
            query1.setName(rs.getString("name"));
            query1.setLogin(rs.getString("login"));
            return query1;
        });
    }

    public List<Query2DTO> query2() {
        String query = "SELECT c.message, g.name AS author_name  " +
                "FROM Commits c  " +
                "JOIN git_user g ON c.author_id = g.user_id " +
                "WHERE g.user_id=856 ";

        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Query2DTO query2 = new Query2DTO();
            query2.setAuthorName(rs.getString("author_name"));
            query2.setCommitMessage(rs.getString("message"));
            return query2;
        });
    }

    public List<Query2DTO> query2_2() {
        String query = "SELECT c.message " +
                "FROM Commits c  " +
                "LIMIT 1000000 ";
        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Query2DTO query2 = new Query2DTO();
            query2.setCommitMessage(rs.getString("message"));
            return query2;
        });
    }

    public List<Query3DTOPostgres> query3() {
        String query = "SELECT g.user_id, g.name, r.repo_id, r.name AS repo_name  " +
                "FROM git_user g   " +
                "JOIN Repository r ON g.user_id = r.owner_id " +
                "WHERE g.user_id=704 ";

        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Query3DTOPostgres query3 = new Query3DTOPostgres();
            query3.setUserId(rs.getInt("user_id"));
            query3.setUserName(rs.getString("name"));
            query3.setRepoId(rs.getInt("repo_id"));
            query3.setRepoName(rs.getString("repo_name"));
            return query3;
        });
    }

    public List<Query4DTOPostgres> query4() {
        String query = "SELECT u.user_id AS user_id, u.name AS user_name, f.follower_id AS follower_id, fol.following_id AS following_id, r.repo_id AS repo_id, r.name AS repo_name, c.commit_id AS commit_id, c.message AS commit_message " +
                "FROM git_user u " +
                "LEFT JOIN followers f ON u.user_id = f.user_id " +
                "LEFT JOIN Following fol ON u.user_id = fol.user_id " +
                "LEFT JOIN UserRepositories ur ON u.user_id = ur.user_id " +
                "LEFT JOIN Repository r ON ur.repo_id = r.repo_id " +
                "LEFT JOIN Commits c ON r.repo_id = c.repo_id " +
                "WHERE u.user_id=380 " +
                "ORDER BY u.user_id, r.repo_id, c.commit_id ";

        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Query4DTOPostgres query4 = new Query4DTOPostgres();
            query4.setUserId(rs.getInt("user_id"));
            query4.setUserName(rs.getString("user_name"));
            query4.setFollowerId(rs.getInt("follower_id"));
            query4.setFollowingId(rs.getInt("following_id"));
            query4.setRepoId(rs.getInt("repo_id"));
            query4.setRepoName(rs.getString("repo_name"));
            query4.setCommitId(rs.getInt("commit_id"));
            query4.setCommitMessage(rs.getString("commit_message"));
            return query4;
        });
    }

    public List<Query4v2DTO> query4_2() {
        String query = "SELECT g.user_id, g.name AS user_name, r.repo_id, r.name AS repo_name, c.commit_id, c.message AS commit_message " +
                "FROM git_user g " +
                "JOIN Repository r ON g.user_id = r.owner_id " +
                "JOIN Commits c ON r.repo_id = c.repo_id " +
                "WHERE g.user_id=282 ";

        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Query4v2DTO query4 = new Query4v2DTO();
            query4.setUserId(rs.getInt("user_id"));
            query4.setUserName(rs.getString("user_name"));
            query4.setRepoId(rs.getInt("repo_id"));
            query4.setRepoName(rs.getString("repo_name"));
            query4.setCommitId(rs.getInt("commit_id"));
            query4.setCommitMessage(rs.getString("commit_message"));
            return query4;
        });
    }

    public List<Query5DTO> query5() {
        String query = "SELECT location, COUNT(user_id) AS user_count " +
                "FROM git_user " +
                "WHERE location = 'New York' " +
                "GROUP BY location " +
                "ORDER BY user_count DESC " +
                "LIMIT 100 ";

        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Query5DTO query5 = new Query5DTO();
            query5.setLocation(rs.getString("location"));
            query5.setUserCount(rs.getInt("user_count"));
            return query5;
        });
    }

    public List<Query6DTO> query6() {
        String query = "SELECT u.user_id, u.name, r.repo_id, r.name AS repo_name, DATE_TRUNC('month', c.commit_at) AS commit_month, COUNT(c.commit_id) AS commit_count " +
                "FROM git_user u " +
                "JOIN Repository r ON u.user_id = r.owner_id " +
                "JOIN Commits c ON r.repo_id = c.repo_id " +
                "WHERE u.user_id=176 " +
                "GROUP BY u.user_id, u.name, r.repo_id, r.name, commit_month " +
                "ORDER BY u.user_id, r.repo_id, commit_month ";

        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Query6DTO query6 = new Query6DTO();
            query6.setUserId(rs.getInt("user_id"));
            query6.setName(rs.getString("name"));
            query6.setRepoId(rs.getInt("repo_id"));
            query6.setRepoName(rs.getString("repo_name"));
            query6.setCommitDate(rs.getString("commit_month"));
            query6.setCommitCount(rs.getInt("commit_count"));
            return query6;
        });
    }
}

