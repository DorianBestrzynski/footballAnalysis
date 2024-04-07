package com.masterthesis.footballanalysis.player.controller;

import com.masterthesis.footballanalysis.player.dto.*;
import com.masterthesis.footballanalysis.player.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/player")
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerService playerService;

    @GetMapping
    public ResponseEntity<String> getPlayer(@RequestParam Long playerId) {
        playerService.getPlayer(playerId);
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/pg/top-scorers")
    public ResponseEntity<List<TopScorers>> getTopScorersInAllLeaguesPg() {
        var playerGoals = playerService.getTopScorersInAllLeaguesPg();
        return ResponseEntity.ok(playerGoals);
    }

    @GetMapping("/mongo/top-scorers")
    public ResponseEntity<List<TopScorersMongo>> getTopScorersInAllLeaguesMongo() {
        var playerGoals = playerService.getTopScorersInAllLeaguesMongo();
        return ResponseEntity.ok(playerGoals);
    }

    @GetMapping("/pg/shots")
    public ResponseEntity<List<PlayerShots>> getShotsPg() {
        var playerShots = playerService.getShotsPg();
        return ResponseEntity.ok(playerShots);
    }

    @GetMapping("/pg/assists")
    public ResponseEntity<List<PlayerAssistsStats>> getPlayersAssistsPg() {
        var playerAssists = playerService.getPlayerAssistsStatsPg();
        return ResponseEntity.ok(playerAssists);
    }

    @GetMapping("/mongo/assists")
    public ResponseEntity<List<PlayerAssistsStats>> getPlayersAssistsMongo() {
        var playerAssists = playerService.getPlayerAssistsStatsMongo();
        return ResponseEntity.ok(playerAssists);
    }

    @GetMapping("/mongo/shots")
    public ResponseEntity<List<PlayerShotsMongo>> getShotsMongo() {
        var playerShots = playerService.getShotsMongo();
        return ResponseEntity.ok(playerShots);
    }

    @PostMapping("/mongo/player")
    @ResponseStatus(HttpStatus.CREATED)
    public void createPlayerMongo(@RequestBody Player player) {
        playerService.createPlayer(player);
    }

    @PostMapping("/pg/player")
    @ResponseStatus(HttpStatus.CREATED)
    public void createPlayerPg(@RequestBody Player player) {
        playerService.createPlayerAppearance(player);
    }

    @PostMapping("/pg/players")
    @ResponseStatus(HttpStatus.CREATED)
    public void createPlayersPg(@RequestBody List<Player> players) {
        playerService.createPlayersAppearances(players);
    }

    @PostMapping("/mongo/players")
    @ResponseStatus(HttpStatus.CREATED)
    public void createPlayers(@RequestBody List<Player> players) {
        playerService.createPlayers(players);
    }
}
