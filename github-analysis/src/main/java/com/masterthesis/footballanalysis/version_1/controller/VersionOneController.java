package com.masterthesis.footballanalysis.version_1.controller;

import com.masterthesis.footballanalysis.version_1.dto.*;
import com.masterthesis.footballanalysis.version_1.service.VersionOneService;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/git")
@RequiredArgsConstructor
public class VersionOneController {
    private final VersionOneService service;

    @GetMapping("/pg/query-1")
    public ResponseEntity<List<Query1DTOPostgres>> query1Pg() {
        var result = service.query1Postgres();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/pg/query-1-2")
    public ResponseEntity<List<Query1DTOPostgres>> query1_2Pg() {
        var result = service.query1_2Postgres();
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

    @GetMapping("/pg/query-4-2")
    public ResponseEntity<List<Query4v2DTO>> query4v2Pg() {
        var result = service.query4v2Postgres();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/pg/query-5")
    public ResponseEntity<List<Query5DTO>> query5Pg() {
        var result = service.query5Postgres();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/pg/query-6")
    public ResponseEntity<List<Query6DTO>> query6Pg() {
        var result = service.query6Postgres();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/pg/write-1")
    @ResponseStatus(HttpStatus.CREATED)
    public void write1Pg(@RequestBody FullUserDTO fullUserDTO) {
        service.write1Postgres(fullUserDTO);
    }

    @PostMapping("/pg/write-2")
    @ResponseStatus(HttpStatus.CREATED)
    public void write2Pg(@RequestBody List<WriteCommitDTO> writeCommitDTOS) {
        service.write2Postgres(writeCommitDTOS);
    }

    @PostMapping("/pg/write-3")
    @ResponseStatus(HttpStatus.CREATED)
    public void write3Pg(@RequestBody GitUser gitUser) {
        service.write3Postgres(gitUser);
    }

    @GetMapping("/mongo/query-1")
    public ResponseEntity<List<Query1DTOMongo>> query1Mongo() {
        var result = service.query1Mongo();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/mongo/query-1-2")
    public ResponseEntity<List<Query1DTOMongo>> query1_2Mongo() {
        var result = service.query1_2Mongo();
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
    public ResponseEntity<List<Document>> query4Mongo() {
        var result = service.query4Mongo();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/mongo/query-4-2")
    public ResponseEntity<List<Query4v2DTO>> query4v2Mongo() {
        var result = service.query4v2Mongo();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/mongo/query-5")
    public ResponseEntity<List<Query5DTO>> query5Mongo() {
        var result = service.query5Mongo();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/mongo/query-6")
    public ResponseEntity<List<Document>> query6Mongo() {
        var result = service.query6Mongo();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/mongo/write-1")
    @ResponseStatus(HttpStatus.CREATED)
    public void write1Mongo(@RequestBody FullUserDTO fullUserDTO) {
        service.write1Mongo(fullUserDTO);
    }

    @PostMapping("/mongo/write-2")
    @ResponseStatus(HttpStatus.CREATED)
    public void write2Mongo(@RequestBody List<WriteCommitDTO> writeCommitDTO) {
        service.write2Mongo(writeCommitDTO);
    }

    @PostMapping("/mongo/write-3")
    @ResponseStatus(HttpStatus.CREATED)
    public void write3Mongo(@RequestBody GitUser gitUser) {
        service.write3Mongo(gitUser);
    }
}
