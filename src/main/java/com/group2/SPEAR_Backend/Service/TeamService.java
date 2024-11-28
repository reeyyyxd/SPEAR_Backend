package com.group2.SPEAR_Backend.Service;

import com.group2.SPEAR_Backend.DTO.StatusDTO;
import com.group2.SPEAR_Backend.DTO.TeamDTO;
import com.group2.SPEAR_Backend.Model.Classes;
import com.group2.SPEAR_Backend.Model.ProjectProposal;
import com.group2.SPEAR_Backend.Model.Team;
import com.group2.SPEAR_Backend.Model.User;
import com.group2.SPEAR_Backend.Repository.ProjectProposalRepository;
import com.group2.SPEAR_Backend.Repository.TeamRepository;
import com.group2.SPEAR_Backend.Repository.UserRepository;
import jakarta.transaction.Transactional;
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


    @Transactional
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
        Team savedTeam = tRepo.save(newTeam);

        savedTeam.getMembers().add(leader);
        return tRepo.save(savedTeam);
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

    public StatusDTO openRecruitment(int teamId) {
        Team team = tRepo.findById(teamId)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new NoSuchElementException("Team with ID " + teamId + " not found or has been deleted."));
        team.setRecruitmentOpen(true);
        tRepo.save(team);

        String leaderFullName = uRepo.findFullNameById(team.getLeader().getUid());

        return new StatusDTO(
                team.getGroupName(),
                leaderFullName,
                "Team is open for recruitment"
        );
    }

    public StatusDTO closeRecruitment(int teamId) {
        Team team = tRepo.findById(teamId)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new NoSuchElementException("Team with ID " + teamId + " not found or has been deleted."));
        team.setRecruitmentOpen(false);
        tRepo.save(team);

        String leaderFullName = uRepo.findFullNameById(team.getLeader().getUid());

        return new StatusDTO(
                team.getGroupName(),
                leaderFullName,
                "Team is not available for recruitment"
        );
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

    public List<TeamDTO> getAllActiveTeams() {
        return tRepo.findAllActiveTeams().stream()
                .map(team -> new TeamDTO(
                        team.getTid(),
                        team.getGroupName(),
                        team.getProject().getProjectName(), // Extract projectName
                        team.getProject().getPid(),
                        team.getLeader().getUid(),
                        team.getClassRef().getCid(),
                        team.getMembers().stream().map(User::getUid).toList(),
                        team.isRecruitmentOpen()
                ))
                .toList();
    }

    public TeamDTO getTeamById(int teamId) {
        Team team = tRepo.findById(teamId)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new NoSuchElementException("Team with ID " + teamId + " not found or has been deleted."));
        return new TeamDTO(
                team.getTid(),
                team.getGroupName(),
                team.getProject().getProjectName(), // Extract projectName
                team.getProject().getPid(),
                team.getLeader().getUid(),
                team.getClassRef().getCid(),
                team.getMembers().stream().map(User::getUid).toList(),
                team.isRecruitmentOpen()
        );
    }

}
