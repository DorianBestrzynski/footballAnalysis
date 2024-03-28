package com.masterthesis.footballanalysis.player.team.repository;


import com.masterthesis.footballanalysis.player.dto.TopScorers;
import com.masterthesis.footballanalysis.player.team.dto.GameStats;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
}
