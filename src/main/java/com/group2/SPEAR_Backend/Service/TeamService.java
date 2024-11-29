package com.group2.SPEAR_Backend.Service;

import com.group2.SPEAR_Backend.DTO.StatusDTO;
import com.group2.SPEAR_Backend.DTO.TeamDTO;
import com.group2.SPEAR_Backend.DTO.UserDTO;
import com.group2.SPEAR_Backend.Model.*;
import com.group2.SPEAR_Backend.Repository.ProjectProposalRepository;
import com.group2.SPEAR_Backend.Repository.TeamRecuitmentRepository;
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

    @Autowired
    private TeamRecuitmentRepository trRepo;


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

        newTeam.getMembers().add(leader);
        Team savedTeam = tRepo.save(newTeam);

        if (savedTeam.getMembers().size() > 4) {
            throw new IllegalStateException("Team cannot have more than 5 members.");
        }

        return savedTeam;
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

    @Transactional
    public void kickMember(int teamId, int memberId) {
        Team team = tRepo.findById(teamId)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new NoSuchElementException("Team with ID " + teamId + " not found or has been deleted."));

        Optional<User> memberToRemove = team.getMembers().stream()
                .filter(member -> member.getUid() == memberId)
                .findFirst();

        if (memberToRemove.isPresent()) {
            team.getMembers().remove(memberToRemove.get());

            Optional<TeamRecuitment> recruitmentRecord = trRepo.findByTeamIdAndStudentId(teamId, memberId);

            if (recruitmentRecord.isPresent()) {
                TeamRecuitment recruitment = recruitmentRecord.get();
                recruitment.setStatus(TeamRecuitment.Status.REJECTED);
                recruitment.setReason("Kicked by team leader.");
                trRepo.save(recruitment);
            } else {
                TeamRecuitment newRecruitmentRecord = new TeamRecuitment();
                newRecruitmentRecord.setTeam(team);
                newRecruitmentRecord.setStudent(memberToRemove.get());
                newRecruitmentRecord.setStatus(TeamRecuitment.Status.REJECTED);
                newRecruitmentRecord.setReason("Kicked by team leader (direct addition).");
                trRepo.save(newRecruitmentRecord);
            }

            tRepo.save(team);
        } else {
            throw new NoSuchElementException("Member with ID " + memberId + " not found in the team.");
        }
    }


    public List<TeamDTO> getAllActiveTeams() {
        return tRepo.findAllActiveTeams().stream()
                .map(team -> new TeamDTO(
                        team.getTid(),
                        team.getGroupName(),
                        team.getProject().getProjectName(),
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
                team.getProject().getProjectName(),
                team.getProject().getPid(),
                team.getLeader().getUid(),
                team.getClassRef().getCid(),
                team.getMembers().stream().map(User::getUid).toList(),
                team.isRecruitmentOpen()
        );
    }

    @Transactional
    public Team transferLeadership(int teamId, int newLeaderId) {
        Team team = tRepo.findById(teamId)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new NoSuchElementException("Team with ID " + teamId + " not found or has been deleted."));

        User newLeader = uRepo.findById(newLeaderId)
                .orElseThrow(() -> new NoSuchElementException("User with ID " + newLeaderId + " not found."));

        if (!team.getMembers().contains(newLeader)) {
            throw new IllegalStateException("The new leader must be a current member of the team.");
        }

        team.setLeader(newLeader);
        return tRepo.save(team);
    }

    @Transactional
    public Team addPreferredMember(int teamId, int memberId) {
        Team team = tRepo.findById(teamId)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new NoSuchElementException("Team with ID " + teamId + " not found or has been deleted."));

        if (team.getMembers().size() >= 4) {
            throw new IllegalStateException("Team already has the maximum number of members.");
        }

        User newMember = uRepo.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("User with ID " + memberId + " not found."));

        Classes teamClass = team.getClassRef();

        if (!teamClass.getEnrolledStudents().contains(newMember)) {
            throw new IllegalStateException("The new member must be enrolled in the same class as the team.");
        }

        team.getMembers().add(newMember);

        return tRepo.save(team);
    }







}
