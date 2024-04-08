package com.masterthesis.footballanalysis.player.repository;

import com.masterthesis.footballanalysis.player.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

    public List<PlayerAssistsStats> getPlayerAssistStats() {
        String query = "SELECT " +
                "    p.name AS PlayerName, " +
                "    SUM(a.assists) AS TotalAssists, " +
                "    COUNT(a.gameID) AS GamesPlayed, " +
                "    ROUND(SUM(a.assists) * 1.0 / COUNT(a.gameID), 2) AS AvgAssistsPerGame " +
                "FROM " +
                "    appearances a " +
                "JOIN " +
                "    players p ON a.playerID = p.playerID " +
                "GROUP BY " +
                "    p.name " +
                "HAVING " +
                "     COUNT(a.gameID) > 5 " + // Considering only players who played more than 5 games
                "ORDER BY " +
                "    AvgAssistsPerGame DESC";

        return jdbcTemplate.query(query, (rs, rowNum) -> {
            PlayerAssistsStats stats = new PlayerAssistsStats();
            stats.setPlayerName(rs.getString("PlayerName"));
            stats.setTotalAssists(rs.getInt("TotalAssists"));
            stats.setTotalGames(rs.getLong("GamesPlayed"));
            stats.setAvgAssistsPerGame(rs.getDouble("AvgAssistsPerGame"));
            return stats;
        });
    }

    @Transactional
    public void createPlayerAndAppearances(Player player) {
        int playerId = createPlayer(player);
        for (PlayerAppearance appearance : player.getAppearances()) {
            createAppearance(appearance, playerId);
        }
    }

    private int createPlayer(Player player) {
        final String sql = "INSERT INTO players (name) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, new String[] {"playerid"}); // Assuming 'playerID' is the generated column name
                    ps.setString(1, player.getName());
                    return ps;
                },
                keyHolder);

        return keyHolder.getKey().intValue();
    }

    // Method to insert a new appearance
    private void createAppearance(PlayerAppearance appearance, int playerId) {
        String sql = "INSERT INTO appearances (gameID, leagueid, playerid, time, goals, ownGoals, xGoals, assists, position, positionOrder, xAssists, shots, keyPasses, yellowCard, redCard, xGoalsChain, xGoalsBuildup) VALUES (?, ?,  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                appearance.getGameID(),
                appearance.getLeagueID(),
                playerId,
                appearance.getTime(),
                appearance.getGoals(),
                appearance.getOwnGoals(),
                appearance.getXGoals(),
                appearance.getAssists(),
                appearance.getPosition(),
                appearance.getPositionOrder(),
                appearance.getXAssists(),
                appearance.getShots(),
                appearance.getKeyPasses(),
                appearance.getYellowCards(),
                appearance.getRedCards(),
                appearance.getXGoalsChain(),
                appearance.getXGoalsBuildup()
        );
    }

    @Transactional
    public void createPlayersAndAppearances(List<Player> players) {
        List<Integer> playerIds = createPlayers(players); // Batch insert players and collect their IDs
        createAppearancesForPlayers(players, playerIds); // Use the collected IDs to batch insert appearances
    }

    private List<Integer> createPlayers(List<Player> players) {
        final String sql = "INSERT INTO players (name) VALUES (?)";
        List<Integer> playerIds = new ArrayList<>();

        for (Player player : players) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(sql, new String[]{"playerid"}); // The column that will be auto-generated
                        ps.setString(1, player.getName());
                        return ps;
                    },
                    keyHolder);

            playerIds.add(keyHolder.getKey().intValue()); // Collect each generated player ID
        }

        return playerIds; // Return the list of generated IDs
    }

    private void createAppearancesForPlayers(List<Player> players, List<Integer> playerIds) {
        final String sql = "INSERT INTO appearances (playerID, gameID, leagueID, time, goals, ownGoals, xGoals, assists, position, positionOrder, xAssists, shots, keyPasses, yellowCard, redCard, xGoalsChain, xGoalsBuildup) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            int playerId = playerIds.get(i); // Corresponding player ID

            for (PlayerAppearance appearance : player.getAppearances()) {
                Object[] appearanceValues = {
                        playerId,
                        appearance.getGameID(),
                        appearance.getLeagueID(),
                        appearance.getTime(),
                        appearance.getGoals(),
                        appearance.getOwnGoals(),
                        appearance.getXGoals(),
                        appearance.getAssists(),
                        appearance.getPosition(),
                        appearance.getPositionOrder(),
                        appearance.getXAssists(),
                        appearance.getShots(),
                        appearance.getKeyPasses(),
                        appearance.getYellowCards(),
                        appearance.getRedCards(),
                        appearance.getXGoalsChain(),
                        appearance.getXGoalsBuildup()
                };

                batchArgs.add(appearanceValues);
            }
        }
        jdbcTemplate.batchUpdate(sql, batchArgs);
    }
}
