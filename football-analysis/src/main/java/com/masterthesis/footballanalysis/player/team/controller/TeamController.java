package com.masterthesis.footballanalysis.player.team.controller;

import com.masterthesis.footballanalysis.player.team.dto.*;
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

    @GetMapping("/pg/goals-season")
    public ResponseEntity<List<TeamGoalsPerLeagueAndSeason>> getGoalsStatsPerLeagueAndSeasonPg() {
        var teamGoalsPerLeagueAndSeasons = teamService.getGoalsStatsPerLeagueAndSeasonPg();
        return ResponseEntity.ok(teamGoalsPerLeagueAndSeasons);
    }

    @GetMapping("/mongo/goals-season")
    public ResponseEntity<List<TeamGoalsPerLeagueAndSeason>> getGoalsStatsPerLeagueAndSeasonMongo() {
        var teamGoalsPerLeagueAndSeasons = teamService.getGoalsStatsPerLeagueAndSeasonMongo();
        return ResponseEntity.ok(teamGoalsPerLeagueAndSeasons);
    }

    @GetMapping("/mongo/goals-match")
    public ResponseEntity<List<GoalsInMatch>> getGoalsInMatchMongo() {
        var goalsInMatches = teamService.getGoalsInMatch();
        return ResponseEntity.ok(goalsInMatches);
    }

    @PostMapping("/pg/stats")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createTeamStatsPg(@RequestBody List<TeamStat> teamStats) {
        teamService.createTeamStatsPg(teamStats);
    }

    @PostMapping("/pg/stat")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createTeamStatPg(@RequestBody TeamStat teamStat) {
        teamService.createTeamStatPg(teamStat);
    }

    @PostMapping("/mongo/stat")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createTeamStatsMongo(@RequestBody TeamStatMongo teamStat) {
        teamService.createTeamStatMongo(teamStat);
    }

    @PostMapping("/mongo/stats")
    public ResponseEntity<MongoBulkStats> createTeamStatsMongo(@RequestBody List<TeamStatMongo> teamStats) {
        var bulkStats = teamService.createTeamStatsMongo(teamStats);
        return new ResponseEntity<>(bulkStats, HttpStatus.CREATED);
    }
}
