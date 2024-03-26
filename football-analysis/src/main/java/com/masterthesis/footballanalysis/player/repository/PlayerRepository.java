package com.masterthesis.footballanalysis.player.repository;

import com.masterthesis.footballanalysis.player.dto.Player;
import com.masterthesis.footballanalysis.player.dto.PlayerGoals;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
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

    public List<PlayerGoals> getPlayerGoals(Long playerId) {
        String query = "SELECT p.playername as playerName, g.gameid as gameId, " +
                "t.teamname as teamName, COUNT(s.shotid) as goals " +
                "FROM Players p " +
                "JOIN Shots s ON p.playerid = s.playerid AND s.shotresult='GOAL'" +
                "JOIN Games g ON s.gameid = g.gameid " +
                "JOIN TeamStats ts ON g.gameid = ts.game_id AND p.teamid = ts.teamid " +
                "WHERE p.playerid = ? AND ts.result = 'W'" +
                "JOIN Teams t ON t.teamid=ts.teamid " +
                "GROUP BY p.player_name, g.game_id, t.team_name;";
        return jdbcTemplate.queryForList(query, PlayerGoals.class, playerId);
    }
}
