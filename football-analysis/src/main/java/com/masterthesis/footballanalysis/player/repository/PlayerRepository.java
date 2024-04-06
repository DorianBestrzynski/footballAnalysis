package com.masterthesis.footballanalysis.player.repository;

import com.masterthesis.footballanalysis.player.dto.Player;
import com.masterthesis.footballanalysis.player.dto.PlayerShots;
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
        String query = "SELECT g.season AS Season, l.name AS LeagueName, p.name AS PlayerName, SUM(a.goals) AS TotalGoals, SUM(a.assists) AS TotalAssists " +
                "FROM appearances a " +
                "JOIN players p ON a.playerid = p.playerID " +
                "JOIN games g ON a.gameid = g.gameID " +
                "JOIN leagues l ON g.leagueID = l.leagueID " +
                "GROUP BY g.season, l.name, p.name " +
                "ORDER BY TotalGoals DESC, TotalAssists DESC";
       return jdbcTemplate.query(query, (rs, rowNum) -> {
           TopScorers scorer = new TopScorers();
           scorer.setSeason(rs.getInt("Season"));
           scorer.setLeagueName(rs.getString("LeagueName"));
           scorer.setPlayerName(rs.getString("PlayerName"));
           scorer.setTotalGoals(rs.getLong("TotalGoals"));
           scorer.setTotalAssists(rs.getLong("TotalAssists"));
           return scorer;
       });
    }

    public List<PlayerShots> getShots() {
        String query = "SELECT p.name AS Name, COUNT(s.shotId) " +
                "FROM shots s " +
                "JOIN players p ON p.playerId = s.shooterId " +
                "GROUP BY p.name " +
                "ORDER BY 2 DESC ";
        return jdbcTemplate.query(query, (rs, rowNum) -> {
            PlayerShots scorer = new PlayerShots();
            scorer.setName(rs.getString("Name"));
            scorer.setCount(rs.getInt("Count"));
            return scorer;
        });
    }
}
