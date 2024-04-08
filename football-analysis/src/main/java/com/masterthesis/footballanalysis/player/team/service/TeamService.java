package com.masterthesis.footballanalysis.player.team.service;

import com.masterthesis.footballanalysis.player.team.dto.*;
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

    public List<TeamGoalsPerLeagueAndSeason> getGoalsStatsPerLeagueAndSeasonPg() {
        return teamRepository.getGoalsStatsPerLeagueAndSeason();
    }

    public List<TeamGoalsPerLeagueAndSeason> getGoalsStatsPerLeagueAndSeasonMongo() {
        return mongoTeamRepository.getGoalsStatsPerLeagueAndSeason();
    }

    public List<GoalsInMatch> getGoalsInMatch() {
        return mongoTeamRepository.goalsInMatch();
    }


    @Transactional
    public void createTeamStatsPg(List<TeamStat> teamStats) {
        teamRepository.createTeamStats(teamStats);
    }

    @Transactional
    public void createTeamStatPg(TeamStat teamStat) {
        teamRepository.createTeamStat(teamStat);
    }

    public void createTeamStatMongo(TeamStatMongo teamStat) {
        mongoTeamRepository.updateTeamStats(teamStat);
    }

    public MongoBulkStats createTeamStatsMongo(List<TeamStatMongo> teamStats) {
        return mongoTeamRepository.updateTeamStatsBatch(teamStats);
    }
}
