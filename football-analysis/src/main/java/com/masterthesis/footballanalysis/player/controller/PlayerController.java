package com.masterthesis.footballanalysis.player.controller;

import com.masterthesis.footballanalysis.player.dto.PlayerShots;
import com.masterthesis.footballanalysis.player.dto.PlayerShotsMongo;
import com.masterthesis.footballanalysis.player.dto.TopScorers;
import com.masterthesis.footballanalysis.player.dto.TopScorersMongo;
import com.masterthesis.footballanalysis.player.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/mongo/shots")
    public ResponseEntity<List<PlayerShotsMongo>> getShotsMongo() {
        var playerShots = playerService.getShotsMongo();
        return ResponseEntity.ok(playerShots);
    }
}
