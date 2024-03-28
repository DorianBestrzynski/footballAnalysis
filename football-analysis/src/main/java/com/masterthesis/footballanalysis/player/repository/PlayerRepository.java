package com.masterthesis.footballanalysis.player.repository;

import com.masterthesis.footballanalysis.player.dto.Player;
import com.masterthesis.footballanalysis.player.dto.TopScorers;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PlayerRepository {
    private final JdbcTemplate jdbcTemplate;

    public void getPlayer() {
        String sql = "SELECT * FROM players";

        List<Player> players = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Player.class));
        System.out.println(players);
    }

    public List<TopScorers> getTopScorersInAllLeagues() {
        String query = "SELECT l.name AS LeagueName, p.name AS PlayerName, SUM(a.goals) AS TotalGoals, SUM(a.assists) AS TotalAssists " +
                "FROM appearances a " +
                "JOIN players p ON a.playerid = p.playerID " +
                "JOIN games g ON a.gameid = g.gameID " +
                "JOIN leagues l ON g.leagueID = l.leagueID " +
                "GROUP BY l.name, p.name " +
                "ORDER BY TotalGoals DESC, TotalAssists DESC";
       return jdbcTemplate.query(query, (rs, rowNum) -> {
           TopScorers scorer = new TopScorers();
           scorer.setLeagueName(rs.getString("LeagueName"));
           scorer.setPlayerName(rs.getString("PlayerName"));
           scorer.setTotalGoals(rs.getLong("TotalGoals"));
           scorer.setTotalAssists(rs.getLong("TotalAssists"));
           return scorer;
       });
    }
}
