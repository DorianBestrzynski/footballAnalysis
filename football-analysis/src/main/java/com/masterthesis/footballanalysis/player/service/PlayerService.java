package com.masterthesis.footballanalysis.player.service;

import com.masterthesis.footballanalysis.player.dto.TopScorers;
import com.masterthesis.footballanalysis.player.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;

    public void getPlayer(Long playerId) {
        playerRepository.getPlayer();
    }

    public List<TopScorers> getTopScorersInAllLeagues() {
        return playerRepository.getTopScorersInAllLeagues();
    }
}
