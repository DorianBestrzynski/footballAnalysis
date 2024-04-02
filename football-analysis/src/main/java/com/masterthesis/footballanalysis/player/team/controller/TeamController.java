package com.masterthesis.footballanalysis.player.team.controller;

import com.masterthesis.footballanalysis.player.dto.TopScorers;
import com.masterthesis.footballanalysis.player.team.dto.GameStats;
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

    @GetMapping("/intense-matches")
    public ResponseEntity<List<GameStats>> getMostIntenseMatches() {
        var gameStats = teamService.getMostIntenseMatches();
        return ResponseEntity.ok(gameStats);
    }

    @PostMapping("/stats")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createTeamStats(@RequestBody List<TeamStat> teamStats) {
        teamService.createTeamStats(teamStats);
    }
}
