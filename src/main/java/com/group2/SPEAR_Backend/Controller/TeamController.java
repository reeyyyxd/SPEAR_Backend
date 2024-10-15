package com.group2.SPEAR_Backend.Controller;

import com.group2.SPEAR_Backend.Entity.TeamEntity;
import com.group2.SPEAR_Backend.Service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
@CrossOrigin(origins = "http://localhost:3000")
public class TeamController {

    @Autowired
    TeamService teamService;

    // Create a new team
    @PostMapping("/create")
    public TeamEntity createTeam(@RequestBody TeamEntity team) {
        return teamService.createTeam(team);
    }

    // Get all teams
    @GetMapping("/all")
    public List<TeamEntity> getAllTeams() {
        return teamService.getAllTeams();
    }

    // Update a team by id
    @PutMapping("/update/{id}")
    public TeamEntity updateTeam(@PathVariable int id, @RequestBody TeamEntity updatedTeam) {
        return teamService.updateTeam(id, updatedTeam);
    }

    // Delete a team by id
    @DeleteMapping("/delete/{id}")
    public String deleteTeam(@PathVariable int id) {
        return teamService.deleteTeam(id);
    }
}
