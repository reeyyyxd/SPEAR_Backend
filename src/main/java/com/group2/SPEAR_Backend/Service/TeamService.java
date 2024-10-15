package com.group2.SPEAR_Backend.Service;

import com.group2.SPEAR_Backend.Entity.TeamEntity;
import com.group2.SPEAR_Backend.Repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TeamService {

    @Autowired
    TeamRepository teamRepository;

    // Create a new team
    public TeamEntity createTeam(TeamEntity team) {
        return teamRepository.save(team);
    }

    // Get all teams
    public List<TeamEntity> getAllTeams() {
        return teamRepository.findAll();
    }

    // Update a team
    public TeamEntity updateTeam(int id, TeamEntity updatedTeam) {
        try {
            TeamEntity existingTeam = teamRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Team with id " + id + " not found"));

            existingTeam.setGroupNo(updatedTeam.getGroupNo());
            existingTeam.setMemberId(updatedTeam.getMemberId());

            return teamRepository.save(existingTeam);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Team with id " + id + " not found");
        }
    }

    // Delete a team
    public String deleteTeam(int id) {
        if (teamRepository.existsById(id)) {
            teamRepository.deleteById(id);
            return "Team with id " + id + " deleted";
        } else {
            return "Team with id " + id + " not found";
        }
    }
}
