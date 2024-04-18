package com.masterthesis.footballanalysis.version_1.service;

import com.masterthesis.footballanalysis.version_1.dto.*;
import com.masterthesis.footballanalysis.version_1.repository.MongoDbRepository;
import com.masterthesis.footballanalysis.version_1.repository.PostgresRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VersionOneService {
    private final PostgresRepository postgresRepository;
    private final MongoDbRepository mongoDbRepository;

    public List<Query1DTOPostgres> query1Postgres(int awayGoals) {
        return postgresRepository.query1(awayGoals);
    }

    public List<Query2DTO> query2Postgres() {
        return postgresRepository.query2();
    }

    public List<Query3DTOPostgres> query3Postgres() {
        return postgresRepository.query3();
    }

    public List<Query4DTOPostgres> query4Postgres() {
        return postgresRepository.query4();
    }

    public List<Query5DTO> query5Postgres() {
        return postgresRepository.query5();
    }

    public List<Query6DTO> query6Postgres() {
        return postgresRepository.query6();
    }

    public List<Query7DTO> query7Postgres() {
        return postgresRepository.query7();
    }

    public List<Query8DTOPostgres> query8Postgres() {
        return postgresRepository.query8();
    }

    public List<Query9DTO> query9Postgres() {
        return postgresRepository.query9();
    }

    public List<Query10DTO> query10Postgres() {
        return postgresRepository.query10();
    }

    public List<Query1DTOMongo> query1Mongo(int awayGoals) {
        return mongoDbRepository.query1(awayGoals);
    }

    public List<Query2DTO> query2Mongo() {
        return mongoDbRepository.query2();
    }

    public List<Query3DTOMongo> query3Mongo() {
        return mongoDbRepository.query3();
    }

    public List<Query4DTOMongo> query4Mongo() {
        return mongoDbRepository.query4();
    }

    public List<Query5DTO> query5Mongo() {
        return mongoDbRepository.query5();
    }

    public List<Query6DTO> query6Mongo() {
        return mongoDbRepository.query6();
    }

    public List<Query7DTO> query7Mongo() {
        return mongoDbRepository.query7();
    }

    public List<Query8DTOMongo> query8Mongo() {
        return mongoDbRepository.query8();
    }

    public List<Query9DTO> query9Mongo() {
        return mongoDbRepository.query9();
    }

    public List<Query10DTO> query10Mongo() {
        return mongoDbRepository.query10();
    }
}
