package com.masterthesis.footballanalysis.version_1.service;

import com.masterthesis.footballanalysis.version_1.dto.*;
import com.masterthesis.footballanalysis.version_1.repository.MongoDbReadRepository;
import com.masterthesis.footballanalysis.version_1.repository.MongoDbWriteRepository;
import com.masterthesis.footballanalysis.version_1.repository.PostgresReadRepository;
import com.masterthesis.footballanalysis.version_1.repository.PostgresWriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VersionOneService {
    private final PostgresReadRepository postgresReadRepository;
    private final PostgresWriteRepository postgresWriteRepository;

    private final MongoDbReadRepository mongoDbReadRepository;
    private final MongoDbWriteRepository mongoDbWriteRepository;

    public List<Query1DTOPostgres> query1Postgres(int awayGoals) {
        return postgresReadRepository.query1(awayGoals);
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

    public List<Query5DTO> query5Postgres() {
        return postgresReadRepository.query5();
    }

    public List<Query6DTO> query6Postgres() {
        return postgresReadRepository.query6();
    }

    public List<Query7DTO> query7Postgres() {
        return postgresReadRepository.query7();
    }

    public List<Query8DTOPostgres> query8Postgres() {
        return postgresReadRepository.query8();
    }

    public List<Query9DTO> query9Postgres() {
        return postgresReadRepository.query9();
    }

    public List<Query10DTO> query10Postgres() {
        return postgresReadRepository.query10();
    }

    public void write1Postgres(Player player) {
        postgresWriteRepository.createPlayerAndAppearancesAndShots(player);
    }

    public void write2Postgres(TeamStat teamStat) {
        postgresWriteRepository.createTeamStat(teamStat);
    }

    public void write3Postgres(Game game) {
        postgresWriteRepository.createGame(game);
    }

    public List<Query1DTOMongo> query1Mongo(int awayGoals) {
        return mongoDbReadRepository.query1(awayGoals);
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

    public List<Query5DTO> query5Mongo() {
        return mongoDbReadRepository.query5();
    }

    public List<Query6DTO> query6Mongo() {
        return mongoDbReadRepository.query6();
    }

    public List<Query7DTO> query7Mongo() {
        return mongoDbReadRepository.query7();
    }

    public List<Query8DTOMongo> query8Mongo() {
        return mongoDbReadRepository.query8();
    }

    public List<Query9DTO> query9Mongo() {
        return mongoDbReadRepository.query9();
    }

    public List<Query10DTO> query10Mongo() {
        return mongoDbReadRepository.query10();
    }

    public void write1Mongo(Player player) {
        mongoDbWriteRepository.updatePlayer(player);
    }

    public void write2Mongo(TeamStatMongo teamStatMongo) {
        mongoDbWriteRepository.updateTeamStats(teamStatMongo);
    }

    public void write3Mongo(Game game) {
        mongoDbWriteRepository.updateGame(game);
    }
}
