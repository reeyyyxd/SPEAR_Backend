//package com.group2.SPEAR_Backend.Service;
//
//import com.group2.SPEAR_Backend.Model.Team;
//import com.group2.SPEAR_Backend.Repository.TeamRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.NoSuchElementException;
//
//@Service
//public class TeamService {
//
//    @Autowired
//    TeamRepository teamRepository;
//
//    // Create a new team
//    public Team createTeam(Team team) {
//        return teamRepository.save(team);
//    }
//
//    // Get all teams
//    public List<Team> getAllTeams() {
//        return teamRepository.findAll();
//    }
//
//    // Update a team
//    public Team updateTeam(int id, Team updatedTeam) {
//        try {
//            Team existingTeam = teamRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Team with id " + id + " not found"));
//
//            existingTeam.setGroupNo(updatedTeam.getGroupNo());
//            existingTeam.setMemberId(updatedTeam.getMemberId());
//
//            return teamRepository.save(existingTeam);
//        } catch (NoSuchElementException e) {
//            throw new NoSuchElementException("Team with id " + id + " not found");
//        }
//    }
//
//    // Delete a team
//    public String deleteTeam(int id) {
//        if (teamRepository.existsById(id)) {
//            teamRepository.deleteById(id);
//            return "Team with id " + id + " deleted";
//        } else {
//            return "Team with id " + id + " not found";
//        }
//    }
//}
