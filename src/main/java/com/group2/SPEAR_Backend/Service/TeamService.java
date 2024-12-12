package com.group2.SPEAR_Backend.Service;

import com.group2.SPEAR_Backend.DTO.FeatureDTO;
import com.group2.SPEAR_Backend.DTO.StatusDTO;
import com.group2.SPEAR_Backend.DTO.TeamDTO;
import com.group2.SPEAR_Backend.DTO.UserDTO;
import com.group2.SPEAR_Backend.Model.*;
import com.group2.SPEAR_Backend.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Autowired
    private FeatureRepository fRepo;


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

        User memberToRemove = team.getMembers().stream()
                .filter(member -> member.getUid() == memberId)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Member with ID " + memberId + " not found in the team."));

        team.getMembers().remove(memberToRemove);

        Optional<TeamRecuitment> recruitmentRecord = trRepo.findByTeamIdAndStudentId(teamId, memberId);
        recruitmentRecord.ifPresent(trRepo::delete);

        tRepo.save(team);
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
        // Fetch active team by ID
        Team team = tRepo.findActiveTeamById(teamId)
                .orElseThrow(() -> new NoSuchElementException("Team with ID " + teamId + " not found or has been deleted."));

        // Fetch the associated project
        ProjectProposal project = ppRepo.findById(team.getProject().getPid())
                .orElseThrow(() -> new NoSuchElementException("Project with ID " + team.getProject().getPid() + " not found."));

        // Fetch features related to the project
        List<FeatureDTO> features = fRepo.findByProjectId(project.getPid()).stream()
                .map(feature -> new FeatureDTO(feature.getFeatureTitle(), feature.getFeatureDescription()))
                .toList();

        // Return TeamDTO with detailed information
        return new TeamDTO(
                team.getTid(),
                team.getGroupName(),
                project.getProjectName(),
                project.getPid(),
                team.getLeader().getUid(),
                team.getClassRef().getCid(),
                team.getMembers().stream().map(User::getUid).toList(),
                team.isRecruitmentOpen(),
                features,
                project.getDescription()
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

    public List<TeamDTO> getTeamsByClassId(int classId) {
        // Fetch active teams for the class
        List<Team> activeTeams = tRepo.findActiveTeamsByClassId(classId);
        if (activeTeams.isEmpty()) {
            throw new NoSuchElementException("No active teams found for class ID " + classId);
        }

        List<TeamDTO> teamDTOs = new ArrayList<>();

        for (Team team : activeTeams) {
            // Ensure team has a project and the project is active
            if (team.getProject() == null || team.getProject().getPid() == 0) {
                throw new IllegalStateException("Team with ID " + team.getTid() + " has no associated project.");
            }

            ProjectProposal project = ppRepo.findById(team.getProject().getPid())
                    .filter(p -> !p.getIsDeleted()) // Ensure the project is not soft-deleted
                    .orElseThrow(() -> new NoSuchElementException("Project with ID " + team.getProject().getPid() + " not found or has been deleted."));

            // Fetch features associated with the project
            List<FeatureDTO> features = fRepo.findByProjectId(project.getPid()).stream()
                    .map(feature -> new FeatureDTO(feature.getFeatureTitle(), feature.getFeatureDescription()))
                    .toList();

            // Map the team details to a TeamDTO
            TeamDTO teamDTO = new TeamDTO(
                    team.getTid(),
                    team.getGroupName(),
                    project.getProjectName(),
                    project.getPid(),
                    team.getLeader().getUid(),
                    team.getClassRef().getCid(),
                    team.getMembers().stream().map(User::getUid).toList(),
                    team.isRecruitmentOpen(),
                    features,
                    project.getDescription()
            );

            teamDTOs.add(teamDTO);
        }

        return teamDTOs;
    }



    public TeamDTO getMyTeam(int userId, int classId) {
        Team team = tRepo.findMyTeamByClassId(userId, classId)
                .orElseThrow(() -> new NoSuchElementException("No team found for the user in this class."));

        String leaderName = team.getLeader().getFirstname() + " " + team.getLeader().getLastname();
        List<String> memberNames = team.getMembers().stream()
                .map(member -> member.getFirstname() + " " + member.getLastname())
                .toList();

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

    public List<UserDTO> getTeamMembers(int teamId) {
        List<User> members = tRepo.findMembersByTeamId(teamId);
        return members.stream()
                .map(member -> new UserDTO(member.getFirstname(), member.getLastname()))
                .toList();
    }










}
