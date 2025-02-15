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

    @Autowired
    private ClassesRepository cRepo;

    @Autowired
    private ScheduleRepository sRepo;


    //reverse function from create project proposal
    //instead of created by member, do it by group
    //fix also the leader functionality
    @Transactional
    public Team createTeam(int studentId, Long classId, String groupName, int adviserId, int scheduleId) {
        User student = uRepo.findById(studentId)
                .orElseThrow(() -> new NoSuchElementException("Student with ID " + studentId + " not found."));

        Classes classRef = cRepo.findById(classId)
                .orElseThrow(() -> new NoSuchElementException("Class with ID " + classId + " not found."));

        if (!classRef.getEnrolledStudents().contains(student)) {
            throw new IllegalStateException("Student must be enrolled in the class to create a team.");
        }

        boolean isAlreadyLeader = tRepo.existsByLeaderAndClassRef(student, classRef);
        if (isAlreadyLeader) {
            throw new IllegalStateException("Student is already leading a team in this class.");
        }

        User adviser = uRepo.findById(adviserId)
                .orElseThrow(() -> new NoSuchElementException("Adviser with ID " + adviserId + " not found."));

        Schedule schedule = sRepo.findById(scheduleId)
                .orElseThrow(() -> new NoSuchElementException("Schedule with ID " + scheduleId + " not found."));

        Team newTeam = new Team(null, student, classRef, groupName, adviser, schedule);
        newTeam.getMembers().add(student);

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
                        team.isRecruitmentOpen(),
                        null,
                        team.getProject().getDescription(),
                        team.getAdviser().getUid(),
                        team.getSchedule().getSchedid()
                ))
                .toList();
    }

    public TeamDTO getTeamById(int teamId) {
        Team team = tRepo.findActiveTeamById(teamId)
                .orElseThrow(() -> new NoSuchElementException("Team with ID " + teamId + " not found or has been deleted."));

        List<FeatureDTO> features = fRepo.findByProjectId(team.getProject().getPid()).stream()
                .map(feature -> new FeatureDTO(feature.getFeatureTitle(), feature.getFeatureDescription()))
                .toList();

        return new TeamDTO(
                team.getTid(),
                team.getGroupName(),
                team.getProject().getProjectName(),
                team.getProject().getPid(),
                team.getLeader().getUid(),
                team.getClassRef().getCid(),
                team.getMembers().stream().map(User::getUid).toList(),
                team.isRecruitmentOpen(),
                features,
                team.getProject().getDescription(),
                team.getAdviser().getUid(),
                team.getSchedule().getSchedid()
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
        List<Team> activeTeams = tRepo.findActiveTeamsByClassId(classId);
        if (activeTeams.isEmpty()) {
            throw new NoSuchElementException("No active teams found for class ID " + classId);
        }

        List<TeamDTO> teamDTOs = new ArrayList<>();
        for (Team team : activeTeams) {
            List<FeatureDTO> features = fRepo.findByProjectId(team.getProject().getPid()).stream()
                    .map(feature -> new FeatureDTO(feature.getFeatureTitle(), feature.getFeatureDescription()))
                    .toList();

            teamDTOs.add(new TeamDTO(
                    team.getTid(),
                    team.getGroupName(),
                    team.getProject().getProjectName(),
                    team.getProject().getPid(),
                    team.getLeader().getUid(),
                    team.getClassRef().getCid(),
                    team.getMembers().stream().map(User::getUid).toList(),
                    team.isRecruitmentOpen(),
                    features,
                    team.getProject().getDescription(),
                    team.getAdviser().getUid(),
                    team.getSchedule().getSchedid()
            ));
        }
        return teamDTOs;
    }

    public TeamDTO getMyTeam(int userId, int classId) {
        Team team = tRepo.findMyTeamByClassId(userId, classId)
                .orElseThrow(() -> new NoSuchElementException("No team found for the user in this class."));

        return new TeamDTO(
                team.getTid(),
                team.getGroupName(),
                team.getProject().getProjectName(),
                team.getProject().getPid(),
                team.getLeader().getUid(),
                team.getClassRef().getCid(),
                team.getMembers().stream().map(User::getUid).toList(),
                team.isRecruitmentOpen(),
                null,
                team.getProject().getDescription(),
                team.getAdviser().getUid(),
                team.getSchedule().getSchedid()
        );
    }

    public List<UserDTO> getTeamMembers(int teamId) {
        List<User> members = tRepo.findMembersByTeamId(teamId);
        return members.stream()
                .map(member -> new UserDTO(member.getFirstname(), member.getLastname()))
                .toList();
    }


    @Transactional
    public void leaveTeam(int teamId, int userId) {
        Team team = tRepo.findById(teamId)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new NoSuchElementException("Team with ID " + teamId + " not found or has been deleted."));

        User user = uRepo.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with ID " + userId + " not found."));

        if (!team.getMembers().contains(user)) {
            throw new IllegalStateException("User is not a member of this team.");
        }
        if (team.getLeader().getUid() == userId) {
            throw new IllegalStateException("Leader cannot leave the team. Transfer leadership or disband the team.");
        }
        team.getMembers().remove(user);
        tRepo.save(team);
    }







}
