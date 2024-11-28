package com.group2.SPEAR_Backend.Service;

import com.group2.SPEAR_Backend.Model.Classes;
import com.group2.SPEAR_Backend.Model.ProjectProposal;
import com.group2.SPEAR_Backend.Model.Team;
import com.group2.SPEAR_Backend.Model.User;
import com.group2.SPEAR_Backend.Repository.ProjectProposalRepository;
import com.group2.SPEAR_Backend.Repository.TeamRepository;
import com.group2.SPEAR_Backend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TeamService {

    @Autowired
    private TeamRepository tRepo;

    @Autowired
    private ProjectProposalRepository ppRepo;

    @Autowired
    private UserRepository uRepo;


    public Team createTeam(int projectId, String groupName) {
        ProjectProposal project = ppRepo.findById(projectId)
                .filter(p -> "APPROVED".equalsIgnoreCase(p.getStatus()))
                .orElseThrow(() -> new NoSuchElementException("Project with ID " + projectId + " is not approved or does not exist."));

        User leader = project.getProposedBy();
        Classes classRef = project.getClassProposal();

        if (leader == null) {
            throw new IllegalStateException("Leader cannot be null. Ensure the proposal has an owner.");
        }

        Team newTeam = new Team(project, leader, classRef, groupName);
        return tRepo.save(newTeam);
    }

    public Team updateGroupName(int teamId, String groupName) {
        Team team = tRepo.findById(teamId)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new NoSuchElementException("Team with ID " + teamId + " not found or has been deleted."));
        team.setGroupName(groupName);
        return tRepo.save(team);
    }

    public String deleteTeam(int teamId) {
        Team team = tRepo.findById(teamId)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new NoSuchElementException("Team with ID " + teamId + " not found or has been deleted."));
        team.setDeleted(true);
        tRepo.save(team);
        return "Team with ID " + teamId + " has been deleted.";
    }

    public Team openRecruitment(int teamId) {
        Team team = tRepo.findById(teamId)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new NoSuchElementException("Team with ID " + teamId + " not found or has been deleted."));
        team.setRecruitmentOpen(true);
        return tRepo.save(team);
    }

    public Team closeRecruitment(int teamId) {
        Team team = tRepo.findById(teamId)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new NoSuchElementException("Team with ID " + teamId + " not found or has been deleted."));
        team.setRecruitmentOpen(false);
        return tRepo.save(team);
    }

    public Team kickMember(int teamId, int memberId) {
        Team team = tRepo.findById(teamId)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new NoSuchElementException("Team with ID " + teamId + " not found or has been deleted."));

        Optional<User> memberToRemove = team.getMembers().stream()
                .filter(member -> member.getUid() == memberId)
                .findFirst();

        if (memberToRemove.isPresent()) {
            team.getMembers().remove(memberToRemove.get());
            return tRepo.save(team);
        } else {
            throw new NoSuchElementException("Member with ID " + memberId + " not found in the team.");
        }
    }

    public List<Team> getAllActiveTeams() {
        return tRepo.findAllActiveTeams();
    }

    public Team getTeamById(int teamId) {
        return tRepo.findById(teamId)
                .filter(team -> !team.isDeleted())
                .orElseThrow(() -> new NoSuchElementException("Team with ID " + teamId + " not found or has been deleted."));
    }
}
