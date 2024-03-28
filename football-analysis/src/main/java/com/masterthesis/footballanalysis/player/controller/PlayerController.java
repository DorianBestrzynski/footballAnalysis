package com.masterthesis.footballanalysis.player.controller;

import com.masterthesis.footballanalysis.player.dto.TopScorers;
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

    @GetMapping("/top-scorers")
    public ResponseEntity<List<TopScorers>> getTopScorersInAllLeagues() {
        var playerGoals = playerService.getTopScorersInAllLeagues();
        return ResponseEntity.ok(playerGoals);
    }
}
