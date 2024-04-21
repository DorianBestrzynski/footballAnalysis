package com.masterthesis.footballanalysis.version_2.repository;

import com.masterthesis.footballanalysis.version_2.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;

@Repository("PostgresWriteRepositoryV2")
@RequiredArgsConstructor
public class PostgresWriteRepository {
    private final JdbcTemplate jdbcTemplate;

    public int createLeague(League league) {
        String sql = "INSERT INTO leagues (name, understatNotation) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, new String[] {"leagueid"});
                    ps.setString(1, league.getName());
                    ps.setString(2, league.getUnderstatNotation());
                    return ps;
                },
                keyHolder);
        return keyHolder.getKey().intValue();
    }

    public int createPlayer(Player player) {
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

    public int createTeam(Team team) {
        final String sql = "INSERT INTO teams (name) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, new String[] {"teamid"}); // Assuming 'playerID' is the generated column name
                    ps.setString(1, team.getName());
                    return ps;
                },
                keyHolder);

        return keyHolder.getKey().intValue();
    }

    public int createGame(Game game, int leagueId, int homeTeamId, int awayTeamId) {
        String sql = "INSERT INTO games (leagueID, season, date, homeTeamID, awayTeamID, homeGoals, awayGoals, homeProbability, drawProbability, awayProbability, homeGoalsHalfTime, awayGoalsHalfTime) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, new String[] {"gameid"});
                    ps.setInt(1, leagueId);
                    ps.setInt(2, game.getSeason());
                    ps.setTimestamp(3, game.getDate());
                    ps.setInt(4, homeTeamId);
                    ps.setInt(5, awayTeamId);
                    ps.setInt(6, game.getHomeGoals());
                    ps.setInt(7, game.getAwayGoals());
                    ps.setDouble(8, game.getHomeProbability());
                    ps.setDouble(9, game.getDrawProbability());
                    ps.setDouble(10, game.getAwayProbability());
                    ps.setInt(11, game.getHomeGoalsHalfTime());
                    ps.setInt(12, game.getAwayGoalsHalfTime());
                    return ps;
                },
                keyHolder);
        return keyHolder.getKey().intValue();
    }

    public int createPlayerAppearance(PlayerAppearance appearance, int gameId, int leagueId, int playerId) {
        String sql = "INSERT INTO appearances (gameID, leagueID, playerID, time, goals, ownGoals, xGoals, assists, position, positionOrder, xAssists, shots, keyPasses, yellowCard, redCard, xGoalsChain, xGoalsBuildup) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    ps.setInt(1, gameId);
                    ps.setInt(2, leagueId);
                    ps.setInt(3, playerId);
                    ps.setInt(4, appearance.getTime());
                    ps.setInt(5, appearance.getGoals());
                    ps.setInt(6, appearance.getOwnGoals());
                    ps.setFloat(7, appearance.getXGoals());
                    ps.setInt(8, appearance.getAssists());
                    ps.setString(9, appearance.getPosition());
                    ps.setInt(10, appearance.getPositionOrder());
                    ps.setFloat(11, appearance.getXAssists());
                    ps.setInt(12, appearance.getShots());
                    ps.setInt(13, appearance.getKeyPasses());
                    ps.setInt(14, appearance.getYellowCard());
                    ps.setInt(15, appearance.getRedCard());
                    ps.setFloat(16, appearance.getXGoalsChain());
                    ps.setFloat(17, appearance.getXGoalsBuildup());
                    return ps;
                },
                keyHolder);
        return 0;
    }

    public int createTeamStat(TeamStat teamStat, int gameId, int teamId) {
        String sql = "INSERT INTO team_stats (gameID, teamID, season, date, location, goals, xGoals, shots, shotsOnTarget, deep, ppda, fouls, corners, yellowCards, redCards, result) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    ps.setInt(1, gameId);
                    ps.setInt(2, teamId);
                    ps.setInt(3, teamStat.getSeason());
                    ps.setTimestamp(4, teamStat.getDate());
                    ps.setString(5, String.valueOf(teamStat.getLocation()));
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
                    ps.setString(16, String.valueOf(teamStat.getResult()));
                    return ps;
                },
                keyHolder);
        return 0;
    }

    public int createShot(Shot shot, int gameId, int shooterId) {
        String sql = "INSERT INTO shots (gameID, shooterID, assisterID, minute, situation, lastAction, shotType, shotResult, xGoal, positionX, positionY) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    ps.setInt(1, gameId);
                    ps.setInt(2, shooterId);
                    ps.setInt(3, shot.getAssisterId());
                    ps.setInt(4, shot.getMinute());
                    ps.setString(5, shot.getSituation());
                    ps.setString(6, shot.getLastAction());
                    ps.setString(7, shot.getShotType());
                    ps.setString(8, shot.getShotResult());
                    ps.setFloat(9, shot.getXGoal());
                    ps.setFloat(10, shot.getPositionX());
                    ps.setFloat(11, shot.getPositionY());
                    return ps;
                },
                keyHolder);
        return 0;
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
