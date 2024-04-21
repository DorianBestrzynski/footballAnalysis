package com.masterthesis.footballanalysis.version_2.service;

import com.masterthesis.footballanalysis.version_2.dto.*;
import com.masterthesis.footballanalysis.version_2.repository.MongoDbReadRepository;
import com.masterthesis.footballanalysis.version_2.repository.MongoDbWriteRepository;
import com.masterthesis.footballanalysis.version_2.repository.PostgresReadRepository;
import com.masterthesis.footballanalysis.version_2.repository.PostgresWriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VersionTwoService {
    private final PostgresReadRepository postgresReadRepository;
    private final PostgresWriteRepository postgresWriteRepository;

    private final MongoDbReadRepository mongoDbReadRepository;
    private final MongoDbWriteRepository mongoDbWriteRepository;

    public List<Query1DTOPostgres> query1Postgres(Timestamp date) {
        return postgresReadRepository.query1(date);
    }

    public List<Query2DTO> query2Postgres() {
        return postgresReadRepository.query2();
    }

    public List<Query3DTOPostgres> query3Postgres() {
        return postgresReadRepository.query3();
    }

    public List<Query4DTOPostgres> query4Postgres() {
        return postgresReadRepository.query4();
    }

    public List<Query5DTOPostgres> query5Postgres() {
        return postgresReadRepository.query5();
    }

    public List<Query6DTOPostgres> query6Postgres() {
        return postgresReadRepository.query6();
    }

    @Transactional
    public void write1Postgres(Write1DTO dto) {
        var leagueId = postgresWriteRepository.createLeague(dto.getLeague());
        var playerId = postgresWriteRepository.createPlayer(dto.getPlayer());
        var teamId1 = postgresWriteRepository.createTeam(dto.getTeam1());
        var teamId2 = postgresWriteRepository.createTeam(dto.getTeam2());
        var gameId = postgresWriteRepository.createGame(dto.getGame(), leagueId, teamId1, teamId2);
        postgresWriteRepository.createPlayerAppearance(dto.getPlayerAppearance(), gameId, leagueId, playerId);
        postgresWriteRepository.createTeamStat(dto.getTeamStat1(), gameId, teamId1);
        postgresWriteRepository.createTeamStat(dto.getTeamStat1(), gameId, teamId2);
        postgresWriteRepository.createShot(dto.getShot(), gameId, playerId);
    }

    public void write2Postgres(TeamStat teamStat) {
        postgresWriteRepository.createTeamStat(teamStat);
    }

    public void write3Postgres(Game game) {
        postgresWriteRepository.createGame(game);
    }

    public List<Query1DTOMongo> query1Mongo(Timestamp date) {
        return mongoDbReadRepository.query1(date);
    }

    public List<Query2DTO> query2Mongo() {
        return mongoDbReadRepository.query2();
    }

    public List<Query3DTOMongo> query3Mongo() {
        return mongoDbReadRepository.query3();
    }

    public List<Query4DTOMongo> query4Mongo() {
        return mongoDbReadRepository.query4();
    }

    public List<Query5DTOMongo> query5Mongo() {
        return mongoDbReadRepository.query5();
    }

    public List<Query6DTOMongo> query6Mongo() {
        return mongoDbReadRepository.query6();
    }

    public void write1Mongo(GameDocument gameDocument) {
        mongoDbWriteRepository.updateTable(gameDocument);
    }

    public void write2Mongo(TeamStatMongo teamStatMongo) {
        mongoDbWriteRepository.updateTeamStats(teamStatMongo);
    }

    public void write3Mongo(Game game) {
        mongoDbWriteRepository.updateGame(game);
    }
}
