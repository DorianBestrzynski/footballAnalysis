package com.masterthesis.footballanalysis.version_1_2.controller;

import com.masterthesis.footballanalysis.version_1_2.dto.*;
import com.masterthesis.footballanalysis.version_1_2.service.VersionOne_TwoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1_2/game")
@RequiredArgsConstructor
public class VersionOne_TwoController {
    private final VersionOne_TwoService service;

    @GetMapping("/mongo/query-1")
    public ResponseEntity<List<Query1DTOMongo>> query1Mongo(@RequestParam int awayGoals) {
        var result = service.query1Mongo(awayGoals);
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
    public ResponseEntity<List<Query5DTO>> query5Mongo() {
        var result = service.query5Mongo();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/mongo/query-6")
    public ResponseEntity<List<Query6DTO>> query6Mongo() {
        var result = service.query6Mongo();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/mongo/query-7")
    public ResponseEntity<List<Query7DTO>> query7Mongo() {
        var result = service.query7Mongo();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/mongo/query-8")
    public ResponseEntity<List<Query8DTOMongo>> query8Mongo() {
        var result = service.query8Mongo();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/mongo/query-9")
    public ResponseEntity<List<Query9DTO>> query9Mongo() {
        var result = service.query9Mongo();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/mongo/query-10")
    public ResponseEntity<List<Query10DTO>> query10Mongo() {
        var result = service.query10Mongo();
        return ResponseEntity.ok(result);
    }
}
