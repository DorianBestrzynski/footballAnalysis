package com.masterthesis.footballanalysis.player.team.repository;

import com.masterthesis.footballanalysis.player.team.dto.GameStats;
import com.masterthesis.footballanalysis.player.team.dto.TeamGoalsPerLeagueAndSeason;
import com.masterthesis.footballanalysis.player.team.dto.TeamStat;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TeamRepository {
    private final JdbcTemplate jdbcTemplate;

    public List<GameStats> getMostIntenseMatches() {
        String query = "SELECT l.name AS LeagueName, " +
                "CONCAT(ht.name, ' vs ', at.name) AS Match, " +
                "g.date, " +
                "SUM(ts.shots) AS TotalShots, " +
                "g.gameId " +
                "FROM games g " +
                "JOIN teams ht ON g.homeTeamID = ht.\"teamId\" " +
                "JOIN teams at ON g.awayTeamID = at.\"teamId\" " +
                "JOIN leagues l ON g.leagueID = l.leagueID " +
                "JOIN team_stats ts ON ts.gameID = g.gameID " +
                "GROUP BY l.name, g.date, ht.name, at.name, g.gameId " +
                "ORDER BY TotalShots DESC";

        return jdbcTemplate.query(query, (rs, rowNum) -> {
            GameStats stats = new GameStats();
            stats.setLeagueName(rs.getString("LeagueName"));
            stats.setMatchName(rs.getString("Match"));
            stats.setDate(rs.getTimestamp("date"));
            stats.setShots(rs.getLong("TotalShots"));
            return stats;
        });
    }

    public List<TeamGoalsPerLeagueAndSeason> getGoalsStatsPerLeagueAndSeason() {
        String query = "SELECT LeagueName, season, TeamName, SUM(TotalGoals) AS TotalGoals, " +
                "SUM(TotalShotsOnTarget) AS TotalShotsOnTarget, SUM(TotalXGoals) AS TotalXGoals, " +
                "CASE WHEN SUM(TotalGoals) > 0 THEN SUM(TotalShotsOnTarget) * 1.0 / SUM(TotalGoals) " +
                "ELSE NULL END AS AvgShotsOnTargetPerGoal " +
                "FROM ( " +
                "SELECT l.name AS LeagueName, g.season, home.name AS TeamName, " +
                "SUM(g.homeGoals) AS TotalGoals, SUM(ts.shotsOnTarget) AS TotalShotsOnTarget, " +
                "SUM(ts.xGoals) AS TotalXGoals " +
                "FROM games g " +
                "INNER JOIN team_stats ts ON g.gameID = ts.gameID AND g.homeTeamID = ts.teamID " +
                "INNER JOIN teams home ON g.homeTeamID = home.\"teamId\" " +
                "INNER JOIN leagues l ON g.leagueID = l.leagueID " +
                "GROUP BY l.name, g.season, home.name " +
                "UNION ALL " +
                "SELECT l.name AS LeagueName, g.season, away.name AS TeamName, " +
                "SUM(g.awayGoals) AS TotalGoals, SUM(ts.shotsOnTarget) AS TotalShotsOnTarget, " +
                "SUM(ts.xGoals) AS TotalXGoals " +
                "FROM games g " +
                "INNER JOIN team_stats ts ON g.gameID = ts.gameID AND g.awayTeamID = ts.teamID " +
                "INNER JOIN teams away ON g.awayTeamID = away.\"teamId\" " +
                "INNER JOIN leagues l ON g.leagueID = l.leagueID " +
                "GROUP BY l.name, g.season, away.name " +
                ") AS combined " +
                "GROUP BY LeagueName, season, TeamName " +
                "ORDER BY LeagueName, season DESC, SUM(TotalGoals) DESC";

        return jdbcTemplate.query(query, (rs, rowNum) -> {
            TeamGoalsPerLeagueAndSeason stats = new TeamGoalsPerLeagueAndSeason();
            stats.setLeagueName(rs.getString("LeagueName"));
            stats.setSeason(rs.getInt("season"));
            stats.setTeamName(rs.getString("TeamName"));
            stats.setTotalGoals(rs.getInt("TotalGoals"));
            stats.setTotalShotsOnTarget(rs.getInt("TotalShotsOnTarget"));
            stats.setTotalXGoals(rs.getDouble("TotalXGoals"));
            stats.setAvgShotsOnTargetPerGoal(rs.getDouble("AvgShotsOnTargetPerGoal")); // Map the new field
            return stats;
        });
    }

    public void createTeamStats(List<TeamStat> teamStatsList) {
        String sql = "INSERT INTO team_stats (gameID, teamID, season, date, location, goals, xGoals, shots, shotsOnTarget, deep, ppda, fouls, corners, yellowCards, redCards, result) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                TeamStat teamStat = teamStatsList.get(i);
                ps.setInt(1, teamStat.getGameID());
                ps.setInt(2, teamStat.getTeamID());
                ps.setInt(3, teamStat.getSeason());
                ps.setTimestamp(4, teamStat.getDate());
                ps.setString(5, String.valueOf(teamStat.getLocation())); // Zakładając, że location jest typu char
                ps.setInt(6, teamStat.getGoals());
                ps.setFloat(7, teamStat.getExpectedGoals());
                ps.setInt(8, teamStat.getShots());
                ps.setInt(9, teamStat.getShotsOnTarget());
                ps.setInt(10, teamStat.getDeep());
                ps.setFloat(11, teamStat.getPpda());
                ps.setInt(12, teamStat.getFouls());
                ps.setInt(13, teamStat.getCorners());
                ps.setFloat(14, teamStat.getYellowCards());
                ps.setInt(15, teamStat.getRedCards());
                ps.setString(16, String.valueOf(teamStat.getResult())); // Zakładając, że result jest typu char
            }

            @Override
            public int getBatchSize() {
                return teamStatsList.size();
            }
        });
    }

    public void createTeamStat(TeamStat teamStat) {
        String sql = "INSERT INTO team_stats (gameID, teamID, season, date, location, goals, xGoals, shots, shotsOnTarget, deep, ppda, fouls, corners, yellowCards, redCards, result) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                teamStat.getGameID(),
                teamStat.getTeamID(),
                teamStat.getSeason(),
                new Timestamp(teamStat.getDate().getTime()), // Assuming getDate() returns a java.util.Date
                String.valueOf(teamStat.getLocation()), // Assuming location is stored as a char or String
                teamStat.getGoals(),
                teamStat.getExpectedGoals(), // Assuming getExpectedGoals() returns a float or double
                teamStat.getShots(),
                teamStat.getShotsOnTarget(),
                teamStat.getDeep(),
                teamStat.getPpda(), // Assuming getPpda() returns a float or double
                teamStat.getFouls(),
                teamStat.getCorners(),
                teamStat.getYellowCards(), // Assuming getYellowCards() returns an integer
                teamStat.getRedCards(),
                String.valueOf(teamStat.getResult()) // Assuming getResult() returns a char or String
        );
    }
}
