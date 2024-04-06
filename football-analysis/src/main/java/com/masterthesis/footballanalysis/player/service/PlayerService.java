package com.masterthesis.footballanalysis.player.service;

import com.masterthesis.footballanalysis.player.dto.PlayerShots;
import com.masterthesis.footballanalysis.player.dto.PlayerShotsMongo;
import com.masterthesis.footballanalysis.player.dto.TopScorers;
import com.masterthesis.footballanalysis.player.dto.TopScorersMongo;
import com.masterthesis.footballanalysis.player.repository.MongoPlayerRepository;
import com.masterthesis.footballanalysis.player.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final MongoPlayerRepository mongoPlayerRepository;

    public void getPlayer(Long playerId) {
        playerRepository.getPlayer();
    }

    public List<TopScorers> getTopScorersInAllLeaguesPg() {
        return playerRepository.getTopScorersInAllLeagues();
    }

    public List<TopScorersMongo> getTopScorersInAllLeaguesMongo() {
        return mongoPlayerRepository.getTopScorersInAllLeagues();
    }

    public List<PlayerShots> getShotsPg() {
        return playerRepository.getShots();
    }

    public List<PlayerShotsMongo> getShotsMongo() {
        return mongoPlayerRepository.getShots();
    }
}
