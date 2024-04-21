package com.masterthesis.footballanalysis.version_1.repository;

import com.masterthesis.footballanalysis.player.team.dto.TeamStat;
import com.masterthesis.footballanalysis.version_1.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Timestamp;

@Repository("PostgresWriteRepositoryV1")
@RequiredArgsConstructor
public class PostgresWriteRepository {
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void createPlayerAndAppearancesAndShots(Player player) {
        int playerId = createPlayer(player);
        for (PlayerAppearance appearance : player.getAppearances()) {
            createAppearance(appearance, playerId);
        }
        for (PlayerShots shot : player.getShots()) {
            createShot(shot, playerId);
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

    private void createShot(PlayerShots shots, int playerId) {
        String sql = "INSERT INTO shots (gameid, shooterid, assisterid, minute, situation, lastaction, shottype, shotresult, xgoal, positionx, positiony) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                shots.getGameID(),
                playerId,
                shots.getAssisterID(),
                shots.getMinute(),
                shots.getSituation(),
                shots.getLastAction(),
                shots.getShotType(),
                shots.getShotResult(),
                shots.getXGoal(),
                shots.getPositionX(),
                shots.getPositionY()
        );
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

    public void createGame(Game game) {
        String sql = "INSERT INTO games (leagueid, season, date, hometeamid, awayteamid, homegoals, awaygoals, homeprobability, drawprobability, awayprobability, homegoalshalftime, awaygoalshalftime) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                game.getLeagueId(),
                game.getSeason(),
                new Timestamp(game.getDate().getTime()), // Assuming getDate() returns a java.util.Date
                game.getHomeTeamId(), // Assuming location is stored as a char or String
                game.getAwayTeamId(),
                game.getHomeGoals(), // Assuming getExpectedGoals() returns a float or double
                game.getAwayGoals(),
                game.getHomeProbability(),
                game.getDrawProbability(),
                game.getAwayProbability(), // Assuming getPpda() returns a float or double
                game.getHomeGoalsHalfTime(),
                game.getAwayGoalsHalfTime()
        );
    }
}
