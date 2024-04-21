package com.masterthesis.footballanalysis.version_2.controller;

import com.masterthesis.footballanalysis.version_2.dto.*;
import com.masterthesis.footballanalysis.version_2.service.VersionTwoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("api/v2/game")
@RequiredArgsConstructor
public class VersionTwoController {
    private final VersionTwoService service;

    @GetMapping("/pg/query-1")
    public ResponseEntity<List<Query1DTOPostgres>> query1Pg(@RequestBody Query1Request date) {
        var result = service.query1Postgres(date.getDate());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/pg/query-2")
    public ResponseEntity<List<Query2DTO>> query2Pg() {
        var result = service.query2Postgres();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/pg/query-3")
    public ResponseEntity<List<Query3DTOPostgres>> query3Pg() {
        var result = service.query3Postgres();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/pg/query-4")
    public ResponseEntity<List<Query4DTOPostgres>> query4Pg() {
        var result = service.query4Postgres();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/pg/query-5")
    public ResponseEntity<List<Query5DTOPostgres>> query5Pg() {
        var result = service.query5Postgres();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/pg/query-6")
    public ResponseEntity<List<Query6DTOPostgres>> query6Pg() {
        var result = service.query6Postgres();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/pg/write-1")
    @ResponseStatus(HttpStatus.CREATED)
    public void write1Pg(@RequestBody Write1DTO write1DTO) {
        service.write1Postgres(write1DTO);
    }

    @PostMapping("/pg/write-2")
    @ResponseStatus(HttpStatus.CREATED)
    public void write2Pg(@RequestBody TeamStat teamStat) {
        service.write2Postgres(teamStat);
    }

    @PostMapping("/pg/write-3")
    @ResponseStatus(HttpStatus.CREATED)
    public void write3Pg(@RequestBody Game game) {
        service.write3Postgres(game);
    }

    @GetMapping("/mongo/query-1")
    public ResponseEntity<List<Query1DTOMongo>> query1Mongo(@RequestBody Query1Request date) {
        var result = service.query1Mongo(date.getDate());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/mongo/query-2")
    public ResponseEntity<List<Query2DTO>> query2Mongo() {
        var result = service.query2Mongo();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/mongo/query-3")
    public ResponseEntity<List<Query3DTOMongo>> query3Mongo() {
        var result = service.query3Mongo();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/mongo/query-4")
    public ResponseEntity<List<Query4DTOMongo>> query4Mongo() {
        var result = service.query4Mongo();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/mongo/query-5")
    public ResponseEntity<List<Query5DTOMongo>> query5Mongo() {
        var result = service.query5Mongo();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/mongo/query-6")
    public ResponseEntity<List<Query6DTOMongo>> query6Mongo() {
        var result = service.query6Mongo();
        return ResponseEntity.ok(result);
    }
    @PostMapping("/mongo/write-1")
    @ResponseStatus(HttpStatus.CREATED)
    public void write1Mongo(@RequestBody GameDocument gameDocument) {
        service.write1Mongo(gameDocument);
    }

    @PostMapping("/mongo/write-2")
    @ResponseStatus(HttpStatus.CREATED)
    public void write2Mongo(@RequestBody TeamStatMongo teamStatMongo) {
        service.write2Mongo(teamStatMongo);
    }

    @PostMapping("/mongo/write-3")
    @ResponseStatus(HttpStatus.CREATED)
    public void write3Mongo(@RequestBody Game game) {
        service.write3Mongo(game);
    }
}
