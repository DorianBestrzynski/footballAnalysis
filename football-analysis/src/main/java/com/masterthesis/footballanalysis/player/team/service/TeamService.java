package com.masterthesis.footballanalysis.player.team.service;

import com.masterthesis.footballanalysis.player.team.dto.GameStats;
import com.masterthesis.footballanalysis.player.team.dto.GameStatsMongo;
import com.masterthesis.footballanalysis.player.team.dto.TeamStat;
import com.masterthesis.footballanalysis.player.team.repository.MongoTeamRepository;
import com.masterthesis.footballanalysis.player.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final MongoTeamRepository mongoTeamRepository;

    public List<GameStats> getMostIntenseMatchesPg() {
        return teamRepository.getMostIntenseMatches();
    }

    public List<GameStatsMongo> getMostIntenseMatchesMongo() {
        return mongoTeamRepository.getMostIntenseMatches();
    }


    @Transactional
    public void createTeamStats(List<TeamStat> teamStats) {
        teamRepository.createTeamStats(teamStats);
    }
}
