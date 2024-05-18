package com.masterthesis.footballanalysis.version_1.service;

import com.masterthesis.footballanalysis.version_1.dto.*;
import com.masterthesis.footballanalysis.version_1.repository.MongoDbReadRepository;
import com.masterthesis.footballanalysis.version_1.repository.MongoDbWriteRepository;
import com.masterthesis.footballanalysis.version_1.repository.PostgresReadRepository;
import com.masterthesis.footballanalysis.version_1.repository.PostgresWriteRepository;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VersionOneService {
    private final PostgresReadRepository postgresReadRepository;
    private final PostgresWriteRepository postgresWriteRepository;

    private final MongoDbReadRepository mongoDbReadRepository;
    private final MongoDbWriteRepository mongoDbWriteRepository;

    public List<Query1DTOPostgres> query1Postgres() {
        return postgresReadRepository.query1();
    }

    public List<Query1DTOPostgres> query1_2Postgres() {
        return postgresReadRepository.query1_2();
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

    public List<Query4v2DTO> query4v2Postgres() {
        return postgresReadRepository.query4_2();
    }

    public List<Query5DTO> query5Postgres() {
        return postgresReadRepository.query5();
    }

    public List<Query6DTO> query6Postgres() {
        return postgresReadRepository.query6();
    }

    public void write1Postgres(FullUserDTO fullUserDTO) {
        postgresWriteRepository.createFullUser(fullUserDTO);
    }

    public void write2Postgres(List<WriteCommitDTO> writeCommitDTOS) {
        if (writeCommitDTOS.isEmpty()) {
            return;
        }
        if (writeCommitDTOS.size() == 1) {
            postgresWriteRepository.createCommit(writeCommitDTOS.get(0));
        } else {
            postgresWriteRepository.createCommits(writeCommitDTOS);
        }
    }

    public void write3Postgres(GitUser gitUser) {
        postgresWriteRepository.createGitUser(gitUser);
    }

    public List<Query1DTOMongo> query1Mongo() {
        return mongoDbReadRepository.query1();
    }

    public List<Query1DTOMongo> query1_2Mongo() {
        return mongoDbReadRepository.query1_2();
    }

    public List<Query2DTO> query2Mongo() {
        return mongoDbReadRepository.query2();
    }

    public List<Query3DTOMongo> query3Mongo() {
        return mongoDbReadRepository.query3();
    }

    public List<Document> query4Mongo() {
        return mongoDbReadRepository.query4();
    }

    public List<Query4v2DTO> query4v2Mongo() {
        return mongoDbReadRepository.query4v2();
    }

    public List<Query5DTO> query5Mongo() {
        return mongoDbReadRepository.query5();
    }

    public List<Document> query6Mongo() {
        return mongoDbReadRepository.query6();
    }

    public void write1Mongo(FullUserDTO fullUserDTO) {
        mongoDbWriteRepository.createFullUser(fullUserDTO);
    }

    public void write2Mongo(List<WriteCommitDTO> writeCommitDTOS) {
        if (writeCommitDTOS.isEmpty()) {
            return;
        }
        var userId = writeCommitDTOS.getFirst().getAuthorId();
        mongoDbWriteRepository.addCommitsToUser(userId, writeCommitDTOS);
    }

    public void write3Mongo(GitUser gitUser) {
        mongoDbWriteRepository.createGitUser(gitUser);
    }
}
