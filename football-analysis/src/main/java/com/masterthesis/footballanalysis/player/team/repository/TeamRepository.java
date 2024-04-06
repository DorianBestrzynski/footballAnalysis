package com.masterthesis.footballanalysis.player.team.repository;

import com.masterthesis.footballanalysis.player.team.dto.GameStats;
import com.masterthesis.footballanalysis.player.team.dto.TeamStat;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TeamRepository {
    private final JdbcTemplate jdbcTemplate;

    public List<GameStats> getMostIntenseMatches() {
        String query = "SELECT l.name AS LeagueName, " +
                "CONCAT(ht.name, ' vs ', at.name) AS Match, " +
                "g.date, " +
                "(SELECT SUM(ts.shots) FROM team_stats ts WHERE ts.gameID = g.gameID) AS TotalShots " +
                "FROM games g " +
                "JOIN teams ht ON g.homeTeamID = ht.\"teamId\" " +
                "JOIN teams at ON g.awayTeamID = at.\"teamId\" " +
                "JOIN leagues l ON g.leagueID = l.leagueID " +
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
}
