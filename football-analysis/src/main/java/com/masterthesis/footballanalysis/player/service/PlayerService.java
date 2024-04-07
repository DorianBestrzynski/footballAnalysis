package com.masterthesis.footballanalysis.player.service;

import com.masterthesis.footballanalysis.player.dto.*;
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

    public List<PlayerAssistsStats> getPlayerAssistsStatsPg() {
        return playerRepository.getPlayerAssistStats();
    }

    public List<PlayerAssistsStats> getPlayerAssistsStatsMongo() {
        return mongoPlayerRepository.getPlayerAssists();
    }

    public List<PlayerShotsMongo> getShotsMongo() {
        return mongoPlayerRepository.getShots();
    }

    public void createPlayer(Player player) {
        mongoPlayerRepository.updatePlayer(player);
    }

    public void createPlayerAppearance(Player player) {
        playerRepository.createPlayerAndAppearances(player);
    }

    public void createPlayersAppearances(List<Player> players) {
        playerRepository.createPlayersAndAppearances(players);
    }

    public void createPlayers(List<Player> players) {
        mongoPlayerRepository.updatePlayers(players);
    }
}
