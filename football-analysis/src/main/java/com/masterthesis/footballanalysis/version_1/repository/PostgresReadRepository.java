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

    public List<Query1DTOPostgres> query1(int awayGoals) {
        String query = "SELECT gameId, leagueId, season, date, homeGoals, awayGoals " +
                "FROM games " +
                "WHERE awayGoals > ? ";
        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Query1DTOPostgres query1 = new Query1DTOPostgres();
            query1.setGameId(rs.getInt("gameId"));
            query1.setLeagueId(rs.getInt("leagueId"));
            query1.setSeason(rs.getInt("season"));
            query1.setDate(rs.getTimestamp("date"));
            query1.setHomeGoals(rs.getInt("homeGoals"));
            query1.setAwayGoals(rs.getInt("awayGoals"));
            return query1;
        }, awayGoals);
    }

    public List<Query2DTO> query2() {
        String query = "SELECT goals, xGoals, shots, shotsontarget, deep, location " +
                "FROM team_stats " +
                "WHERE location = 'h' " +
                "LIMIT 100 ";
        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Query2DTO query2 = new Query2DTO();
            query2.setShots(rs.getInt("shots"));
            query2.setDeep(rs.getInt("deep"));
            query2.setXGoals(rs.getDouble("xGoals"));
            query2.setGoals(rs.getInt("goals"));
            query2.setLocation(rs.getString("location"));
            query2.setShotsontarget(rs.getInt("shotsontarget"));
            return query2;
        });
    }

    public List<Query3DTOPostgres> query3() {
        String query = "SELECT playerId, name, minute, situation, lastaction, shottype, shotresult " +
                "FROM players p " +
                "JOIN shots s ON s.shooterId = p.playerId " +
                "WHERE p.playerId = 555 ";

        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Query3DTOPostgres query3 = new Query3DTOPostgres();
            query3.setPlayerId(rs.getLong("playerId"));
            query3.setName(rs.getString("name"));
            query3.setMinute(rs.getInt("minute"));
            query3.setSituation(rs.getString("situation"));
            query3.setLastAction(rs.getString("lastaction"));
            query3.setShotType(rs.getString("shottype"));
            query3.setShotResult(rs.getString("shotresult"));
            return query3;
        });
    }

    public List<Query4DTOPostgres> query4() {
        String query = "SELECT l.name AS LeagueName,CONCAT(ht.name, ' vs ', at.name) AS Match, g.date, ts.shots AS HomeShots " +
                "FROM leagues l " +
                "JOIN games g ON g.leagueId = l.leagueId " +
                "JOIN teams ht ON g.homeTeamID = ht.teamId " +
                "JOIN teams at ON g.awayTeamID = at.teamId " +
                "LEFT JOIN team_stats ts ON ts.gameID = g.gameID AND ts.teamId = ht.teamId " +
                "ORDER BY ts.shots DESC ";
        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Query4DTOPostgres query4 = new Query4DTOPostgres();
            query4.setLeagueName(rs.getString("LeagueName"));
            query4.setMatch(rs.getString("Match"));
            query4.setDate(rs.getTimestamp("date"));
            query4.setHomeShots(rs.getInt("HomeShots"));
            return query4;
        });
    }

    public List<Query5DTO> query5() {
        String query = "SELECT g.gameId, g.season, s.shottype, s.shotresult " +
                "FROM shots s " +
                "JOIN games g ON s.gameid = g.gameId " +
                "LIMIT 10 ";

        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Query5DTO query5 = new Query5DTO();
            query5.setGameId(rs.getInt("gameId"));
            query5.setSeason(rs.getInt("season"));
            query5.setShotType(rs.getString("shottype"));
            query5.setShotResult(rs.getString("shotresult"));
            return query5;
        });
    }

    public List<Query6DTO> query6() {
        String query = "SELECT p.name AS PlayerName, g.season, l.name AS LeagueName, a.goals " +
                "FROM appearances a " +
                "JOIN players p ON a.playerId = p.playerId " +
                "JOIN games g ON a.gameId = g.gameID " +
                "JOIN leagues l ON l.leagueId = g.leagueId " +
                "LIMIT 10 ";

        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Query6DTO query6 = new Query6DTO();
            query6.setPlayerName(rs.getString("PlayerName"));
            query6.setSeason(rs.getInt("season"));
            query6.setLeagueName(rs.getString("LeagueName"));
            query6.setGoals(rs.getInt("goals"));
            return query6;
        });
    }

    public List<Query7DTO> query7() {
        String query = "SELECT " +
                "    p.name AS PlayerName, " +
                "    SUM(a.assists) AS TotalAssists, " +
                "    COUNT(a.gameID) AS GamesPlayed, " +
                "    ROUND(SUM(a.assists) * 1.0 / COUNT(a.gameID), 2) AS AvgAssistsPerGame " +
                "FROM " +
                "    appearances a " +
                "JOIN " +
                "    players p ON a.playerID = p.playerID " +
                "WHERE p.playerId = 447 " +
                "GROUP BY " +
                "    p.name " +
                "HAVING " +
                "     COUNT(a.gameID) > 5 " + // Considering only players who played more than 5 games
                "ORDER BY " +
                "    AvgAssistsPerGame DESC " +
                "LIMIT 100 ";

        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Query7DTO query7 = new Query7DTO();
            query7.setPlayerName(rs.getString("PlayerName"));
            query7.setTotalAssists(rs.getInt("TotalAssists"));
            query7.setTotalGames(rs.getLong("GamesPlayed"));
            query7.setAvgAssistsPerGame(rs.getDouble("AvgAssistsPerGame"));
            return query7;
        });
    }

    public List<Query8DTOPostgres> query8() {
        String query = "SELECT l.name AS LeagueName, " +
                "CONCAT(ht.name, ' vs ', at.name) AS Match, " +
                "g.date, " +
                "SUM(ts.shots) AS TotalShots, " +
                "g.gameId " +
                "FROM games g " +
                "JOIN teams ht ON g.homeTeamID = ht.teamId " +
                "JOIN teams at ON g.awayTeamID = at.teamId " +
                "JOIN leagues l ON g.leagueID = l.leagueID " +
                "JOIN team_stats ts ON ts.gameID = g.gameID " +
                "WHERE g.gameId = 9887 " +
                "GROUP BY l.name, g.date, ht.name, at.name, g.gameId " +
                "ORDER BY TotalShots DESC ";

        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Query8DTOPostgres query8 = new Query8DTOPostgres();
            query8.setLeagueName(rs.getString("LeagueName"));
            query8.setMatchName(rs.getString("Match"));
            query8.setDate(rs.getTimestamp("date"));
            query8.setShots(rs.getLong("TotalShots"));
            return query8;
        });
    }

    public List<Query9DTO> query9() {
        String query = "SELECT g.season, SUM(s.xgoal) AS xGoalSum " +
                "FROM shots s " +
                "JOIN games g ON s.gameid = g.gameId " +
                "GROUP BY g.season " +
                "LIMIT 100 ";

        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Query9DTO query9 = new Query9DTO();
            query9.setSeason(rs.getInt("season"));
            query9.setXGoalSum(rs.getDouble("xGoalSum"));
            return query9;
        });
    }

    public List<Query10DTO> query10() {
        String query = "SELECT p.name, g.season, SUM(a.goals) AS Goals " +
                "FROM appearances a " +
                "JOIN players p ON a.playerId = p.playerId " +
                "JOIN games g ON a.gameId = g.gameID " +
                "JOIN leagues l ON l.leagueId = g.leagueId " +
                "WHERE p.playerId = 2371  " +
                "GROUP BY p.name, g.season " +
                "ORDER BY g.season, SUM(a.goals) DESC " +
                "LIMIT 100 ";

        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Query10DTO query10 = new Query10DTO();
            query10.setName(rs.getString("name"));
            query10.setSeason(rs.getInt("season"));
            query10.setGoals(rs.getInt("Goals"));
            return query10;
        });
    }

    public List<Query11DTO> query11() {
        String query = "SELECT p.name, s.situation, s.shotResult " +
                "FROM players p " +
                "JOIN shots s ON s.shooterId = p.playerId " +
                "WHERE s.shotResult = 'Goal' " +
                "GROUP BY p.name, s.situation, s.shotResult " +
                "LIMIT 100 ";

        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Query11DTO query11 = new Query11DTO();
            query11.setPlayerName(rs.getString("name"));
            query11.setSituation(rs.getString("situation"));
            query11.setShotResult(rs.getString("shotResult"));
            return query11;
        });
    }
}

