package com.masterthesis.footballanalysis.version_1_2.service;

import com.masterthesis.footballanalysis.version_1_2.dto.*;
import com.masterthesis.footballanalysis.version_1_2.repository.MongoDbReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VersionOne_TwoService {
    private final MongoDbReadRepository mongoDbReadRepository;

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

    public List<Query11DTO> query11Mongo() {
        return mongoDbReadRepository.query11();
    }
}