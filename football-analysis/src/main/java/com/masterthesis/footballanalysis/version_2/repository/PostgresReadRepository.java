package com.masterthesis.footballanalysis.version_2.repository;

import com.masterthesis.footballanalysis.version_2.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository("PostgresReadRepositoryV2")
@RequiredArgsConstructor
public class PostgresReadRepository {
    private final JdbcTemplate jdbcTemplate;

    public List<Query1DTOPostgres> query1(Timestamp date) {
        String query = "SELECT gameId, leagueId, season, date, homeGoals, awayGoals " +
                "FROM games " +
                "WHERE date > ? " +
                "LIMIT 100 ";

        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Query1DTOPostgres query1 = new Query1DTOPostgres();
            query1.setGameId(rs.getInt("gameId"));
            query1.setLeagueId(rs.getInt("leagueId"));
            query1.setSeason(rs.getInt("season"));
            query1.setDate(rs.getTimestamp("date"));
            query1.setHomeGoals(rs.getInt("homeGoals"));
            query1.setAwayGoals(rs.getInt("awayGoals"));
            return query1;
        }, date);
    }

    public List<Query2DTO> query2() {
        String query = "SELECT goals, ownGoals, shots, assists " +
                "FROM appearances " +
                "ORDER BY goals DESC, assists DESC, shots DESC, owngoals DESC " +
                "LIMIT 100000 ";

        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Query2DTO query2 = new Query2DTO();
            query2.setGoals(rs.getInt("goals"));
            query2.setOwnGoals(rs.getInt("ownGoals"));
            query2.setShots(rs.getInt("shots"));
            query2.setAssists(rs.getInt("assists"));
            return query2;
        });
    }

    public List<Query3DTOPostgres> query3() {
        String query = "SELECT g.date, g.season, s.minute, s.situation, s.shotType, s.shotResult " +
                "FROM games g " +
                "JOIN shots s ON g.gameid = s.gameid " +
                "LIMIT 100 ";

        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Query3DTOPostgres query3 = new Query3DTOPostgres();
            query3.setDate(rs.getTimestamp("date"));
            query3.setSeason(rs.getInt("season"));
            query3.setMinute(rs.getInt("minute"));
            query3.setSituation(rs.getString("situation"));
            query3.setShotType(rs.getString("shottype"));
            query3.setShotResult(rs.getString("shotresult"));
            return query3;
        });
    }

    public List<Query4DTOPostgres> query4() {
        String query = "SELECT g.date, g.season, CONCAT(ht.name, ' vs ', at.name) AS Match, " +
                "l.name AS LeagueName, CONCAT(ts_h.goals, ' : ', ts_a.goals) AS Result, " +
                "s.minute, s.situation, s.shotResult, p.name AS PlayerName, a.goals AS PlayerGoals, a.shots AS PlayerShots " +
                "FROM games g " +
                "JOIN shots s ON g.gameid = s.gameid " +
                "JOIN players p ON p.playerId = s.shooterId " +
                "JOIN appearances a ON a.playerId = p.playerId AND g.gameId = a.gameId " +
                "JOIN leagues l ON l.leagueId = g.leagueId " +
                "JOIN teams ht ON ht.teamId = g.homeTeamId " +
                "JOIN teams at ON at.teamId = g.awayTeamId " +
                "JOIN team_stats ts_h ON ts_h.gameId = g.gameId AND ts_h.teamId = ht.teamId " +
                "JOIN team_stats ts_a ON ts_a.gameId = g.gameId AND ts_a.teamId = at.teamId " +
                "LIMIT 100000 ";

        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Query4DTOPostgres query4 = new Query4DTOPostgres();
            query4.setDate(rs.getTimestamp("date"));
            query4.setSeason(rs.getInt("season"));
            query4.setMatch(rs.getString("Match"));
            query4.setLeagueName(rs.getString("LeagueName"));
            query4.setResult(rs.getString("Result"));
            query4.setMinute(rs.getInt("minute"));
            query4.setSituation(rs.getString("situation"));
            query4.setShotResult(rs.getString("shotResult"));
            query4.setPlayerName(rs.getString("PlayerName"));
            query4.setPlayerGoals(rs.getInt("PlayerGoals"));
            query4.setPlayerShots(rs.getInt("PlayerShots"));
            return query4;
        });
    }

    public List<Query5DTOPostgres> query5() {
        String query = "SELECT g.gameId, g.date, g.season, COUNT(s.shotId) AS totalShots, SUM(CASE WHEN s.shotResult = 'Goal' THEN 1 ELSE 0 END) AS goalsScored " +
                "FROM games g " +
                "JOIN shots s ON g.gameID = s.gameID " +
                "WHERE g.gameId = 16129 " +
                "GROUP BY g.gameID, g.date, g.homeTeamID, g.awayTeamID " +
                "ORDER BY g.date DESC, goalsScored DESC " +
                "LIMIT 100 ";

        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Query5DTOPostgres query5 = new Query5DTOPostgres();
            query5.setGameId(rs.getInt("gameId"));
            query5.setDate(rs.getTimestamp("date"));
            query5.setSeason(rs.getInt("season"));
            query5.setTotalShots(rs.getInt("totalShots"));
            query5.setGoalsScored(rs.getInt("goalsScored"));
            return query5;
        });
    }

    public List<Query6DTOPostgres> query6() {
        String query = "SELECT g.date, g.season, CONCAT(ht.name, ' vs ', at.name) AS Match, " +
                "l.name AS LeagueName, CONCAT(ts_h.goals, ' : ', ts_a.goals) AS Result, " +
                "AVG(a.shots) AS AvgShotsPerPlayer, SUM(s.shotId) AS TotalShots, SUM(CASE WHEN s.shotResult = 'Goal' THEN 1 ELSE 0 END) AS TotalGoals, " +
                "s.minute, s.situation, s.shotResult, p.name AS PlayerName, a.goals AS PlayerGoals, a.shots AS PlayerShots " +
                "FROM games g " +
                "JOIN shots s ON g.gameid = s.gameid " +
                "JOIN players p ON p.playerId = s.shooterId " +
                "JOIN appearances a ON a.playerId = p.playerId AND g.gameId = a.gameId " +
                "JOIN leagues l ON l.leagueId = g.leagueId " +
                "JOIN teams ht ON ht.teamId = g.homeTeamId " +
                "JOIN teams at ON at.teamId = g.awayTeamId " +
                "JOIN team_stats ts_h ON ts_h.gameId = g.gameId AND ts_h.teamId = ht.teamId " +
                "JOIN team_stats ts_a ON ts_a.gameId = g.gameId AND ts_a.teamId = at.teamId " +
                "WHERE g.gameId = 16129 " +
                "GROUP BY g.gameID, g.date, g.season, ht.name, at.name, l.name, ts_h.goals, ts_a.goals, s.minute,s.situation, s.shotResult, p.name, a.goals, a.shots " +
                "ORDER BY g.date DESC ";

        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Query6DTOPostgres query6 = new Query6DTOPostgres();
            query6.setDate(rs.getTimestamp("date"));
            query6.setSeason(rs.getInt("season"));
            query6.setMatch(rs.getString("Match"));
            query6.setLeagueName(rs.getString("LeagueName"));
            query6.setResult(rs.getString("Result"));
            query6.setAvgShotsPerPlayer(rs.getDouble("AvgShotsPerPlayer"));
            query6.setTotalShots(rs.getInt("TotalShots"));
            query6.setTotalGoals(rs.getInt("TotalGoals"));
            query6.setMinute(rs.getInt("minute"));
            query6.setSituation(rs.getString("situation"));
            query6.setShotResult(rs.getString("shotResult"));
            query6.setPlayerName(rs.getString("PlayerName"));
            query6.setPlayerGoals(rs.getInt("PlayerGoals"));
            query6.setPlayerShots(rs.getInt("PlayerShots"));
            return query6;
        });
    }
}
