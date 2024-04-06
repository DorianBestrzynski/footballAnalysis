package com.masterthesis.footballanalysis.player.team.controller;

import com.masterthesis.footballanalysis.player.dto.TopScorers;
import com.masterthesis.footballanalysis.player.team.dto.GameStats;
import com.masterthesis.footballanalysis.player.team.dto.GameStatsMongo;
import com.masterthesis.footballanalysis.player.team.dto.TeamStat;
import com.masterthesis.footballanalysis.player.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/team")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @GetMapping("/pg/intense-matches")
    public ResponseEntity<List<GameStats>> getMostIntenseMatchesPg() {
        var gameStats = teamService.getMostIntenseMatchesPg();
        return ResponseEntity.ok(gameStats);
    }

    @GetMapping("/mongo/intense-matches")
    public ResponseEntity<List<GameStatsMongo>> getMostIntenseMatchesMongo() {
        var gameStats = teamService.getMostIntenseMatchesMongo();
        return ResponseEntity.ok(gameStats);
    }

    @PostMapping("/stats")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createTeamStats(@RequestBody List<TeamStat> teamStats) {
        teamService.createTeamStats(teamStats);
    }
}
