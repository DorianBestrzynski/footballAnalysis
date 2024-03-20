package com.masterthesis.footballanalysis.player.repository;

import com.masterthesis.footballanalysis.player.dto.Player;
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
        String sql = "SELECT * FROM players WHERE playerId=?";

        List<Player> players = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Player.class));
        System.out.println(players);
    }
}
