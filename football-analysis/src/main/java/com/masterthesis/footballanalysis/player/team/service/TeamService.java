package com.masterthesis.footballanalysis.player.team.service;

import com.masterthesis.footballanalysis.player.team.dto.GameStats;
import com.masterthesis.footballanalysis.player.team.dto.TeamStat;
import com.masterthesis.footballanalysis.player.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;

    public List<GameStats> getMostIntenseMatches() {
        return teamRepository.getMostIntenseMatches();
    }

    @Transactional
    public void createTeamStats(List<TeamStat> teamStats) {
        teamRepository.createTeamStats(teamStats);
    }
}
