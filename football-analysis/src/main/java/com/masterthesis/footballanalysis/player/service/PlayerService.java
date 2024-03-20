package com.masterthesis.footballanalysis.player.service;

import com.masterthesis.footballanalysis.player.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;

    public void getPlayer(Long playerId) {
        playerRepository.getPlayer();
    }
}
