package com.masterthesis.footballanalysis.player.team.service;

import com.masterthesis.footballanalysis.player.team.dto.GameStats;
import com.masterthesis.footballanalysis.player.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;

    public List<GameStats> getMostIntenseMatches() {
        return teamRepository.getMostIntenseMatches();
    }
}
