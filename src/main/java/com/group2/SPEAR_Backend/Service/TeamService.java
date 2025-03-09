package com.group2.SPEAR_Backend.Service;

import com.group2.SPEAR_Backend.DTO.*;
import com.group2.SPEAR_Backend.Model.*;
import com.group2.SPEAR_Backend.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("h:mm a");


    //reverse function from create project proposal
    //instead of created by member, do it by group
    //fix also the leader functionality
    @Transactional
    public Team createTeam(int studentId, Long classId, String groupName) {
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

        Team newTeam = new Team(null, student, classRef, groupName, null, null);
        newTeam.getMembers().add(student);
        newTeam.setRecruitmentOpen(true);
        return tRepo.save(newTeam);
    }

    //all the leader functions
    @Transactional
    public void addPreferredMember(int teamId, int requesterId, int memberId) {
        Team team = tRepo.findById(teamId)
                .orElseThrow(() -> new NoSuchElementException("Team with ID " + teamId + " not found."));

        if (team.getLeader().getUid() != requesterId) {
            throw new IllegalStateException("Only the team leader can add members.");
        }

        Classes teamClass = team.getClassRef();
        int maxTeamSize = teamClass.getMaxTeamSize();

        if (!team.isRecruitmentOpen()) {
            throw new IllegalStateException("Cannot add members. Recruitment is closed.");
        }

        User newMember = uRepo.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("User with ID " + memberId + " not found."));

        if (!teamClass.getEnrolledStudents().contains(newMember)) {
            throw new IllegalStateException("The new member must be enrolled in the same class as the team.");
        }
        team.getMembers().add(newMember);

        if (team.getMembers().size() >= maxTeamSize) {
            team.setRecruitmentOpen(false);
        }

        tRepo.save(team);
    }

    public Team updateGroupName(int teamId, String groupName, int requesterId) {
        Team team = tRepo.findById(teamId)
                .orElseThrow(() -> new NoSuchElementException("Team with ID " + teamId + " not found."));

        if (team.getLeader().getUid() != requesterId) {
            throw new IllegalStateException("Only the team leader can update the group name.");
        }
        team.setGroupName(groupName);
        return tRepo.save(team);
    }

    public StatusDTO openRecruitment(int teamId, int requesterId) {
        Team team = tRepo.findById(teamId)
                .orElseThrow(() -> new NoSuchElementException("Team with ID " + teamId + " not found."));

        if (team.getLeader().getUid() != requesterId) {
            throw new IllegalStateException("Only the team leader can open recruitment.");
        }

        team.setRecruitmentOpen(true);
        tRepo.save(team);

        String leaderFullName = uRepo.findFullNameById(team.getLeader().getUid());

        return new StatusDTO(
                team.getGroupName(),
                leaderFullName,
                "Team is open for recruitment"
        );
    }

    public StatusDTO closeRecruitment(int teamId, int requesterId) {
        Team team = tRepo.findById(teamId)
                .orElseThrow(() -> new NoSuchElementException("Team with ID " + teamId + " not found."));

        if (team.getLeader().getUid() != requesterId) {
            throw new IllegalStateException("Only the team leader can close recruitment.");
        }

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
    public void kickMember(int teamId, int requesterId, int memberId) {
        Team team = tRepo.findById(teamId)
                .orElseThrow(() -> new NoSuchElementException("Team with ID " + teamId + " not found."));

        if (team.getLeader().getUid() != requesterId) {
            throw new IllegalStateException("Only the team leader can remove members.");
        }

        User memberToRemove = team.getMembers().stream()
                .filter(member -> member.getUid() == memberId)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Member with ID " + memberId + " not found in the team."));

        team.getMembers().remove(memberToRemove);

        Optional<TeamRecuitment> recruitmentRecord = trRepo.findByTeamIdAndStudentId(teamId, memberId);
        recruitmentRecord.ifPresent(trRepo::delete);

        tRepo.save(team);
    }

    @Transactional
    public void transferLeadership(int teamId, int requesterId, int newLeaderId) {
        Team team = tRepo.findById(teamId)
                .orElseThrow(() -> new NoSuchElementException("Team with ID " + teamId + " not found."));

        if (team.getLeader().getUid() != requesterId) {
            throw new IllegalStateException("Only the current leader can transfer leadership.");
        }

        User newLeader = uRepo.findById(newLeaderId)
                .orElseThrow(() -> new NoSuchElementException("User with ID " + newLeaderId + " not found."));

        if (!team.getMembers().contains(newLeader)) {
            throw new IllegalStateException("The new leader must be a current member of the team.");
        }

        team.setLeader(newLeader);
        tRepo.save(team);
    }

    @Transactional
    public void deleteTeam(int teamId, int requesterId) {
        Team team = tRepo.findById(teamId)
                .orElseThrow(() -> new NoSuchElementException("Team not found"));

        if (team.getLeader().getUid() != requesterId) {
            throw new IllegalStateException("Only the team leader can delete the team.");
        }

        if (team.getMembers().size() > 1) {
            throw new IllegalStateException("Cannot delete the team. Remove all members first.");
        }
        team.setSchedule(null);
        tRepo.save(team);
        tRepo.deleteById(teamId);
    }
    //scheduling
    //schedule to change
    public List<ScheduleDTO> getAvailableSchedulesForAdviser(int adviserId, Long classId) {
        List<Schedule> schedules = sRepo.findAvailableSchedulesForAdviserAndClass(adviserId, classId);

        return schedules.stream()
                .map(schedule -> new ScheduleDTO(
                        schedule.getSchedid(),
                        schedule.getDay(),
                        schedule.getStartTime(),
                        schedule.getEndTime(),
                        schedule.getTeacher().getUid(),
                        schedule.getTeacher().getFirstname() + " " + schedule.getTeacher().getLastname(),
                        schedule.getScheduleOfClasses().getCid(),
                        schedule.getScheduleOfClasses().getCourseCode(),
                        schedule.getScheduleOfClasses().getCourseDescription()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public String assignAdviserAndSchedule(int teamId, int adviserId, int scheduleId, int requesterId) {
        Team team = tRepo.findById(teamId)
                .orElseThrow(() -> new NoSuchElementException("Team not found"));

        User requester = uRepo.findById(requesterId)
                .orElseThrow(() -> new NoSuchElementException("Requester not found"));

        // Ensure only the leader can assign an adviser
        if (team.getLeader().getUid() != requesterId) {
            throw new IllegalStateException("Only the team leader can assign an adviser and schedule.");
        }

        User adviser = uRepo.findById(adviserId)
                .orElseThrow(() -> new NoSuchElementException("Adviser not found"));

        Schedule schedule = sRepo.findById(scheduleId)
                .orElseThrow(() -> new NoSuchElementException("Schedule not found"));

        // Check if the schedule is already assigned to another team
        Optional<Team> conflictingTeam = tRepo.findBySchedule(schedule);
        if (conflictingTeam.isPresent() && conflictingTeam.get().getTid() != teamId) {
            throw new IllegalStateException("This schedule is already assigned to another team.");
        }

        team.setAdviser(adviser);
        team.setSchedule(schedule);
        tRepo.save(team);

        return "Adviser and Schedule successfully assigned to the team.";
    }

    //for team recuitment only
    public List<TeamDTO> getOpenTeamsForRecruitment() {
        List<Team> openTeams = tRepo.findOpenTeamsForRecruitment();

        return openTeams.stream()
                .map(team -> new TeamDTO(
                        team.getTid(),
                        team.getGroupName(),
                        (team.getProject() != null) ? team.getProject().getProjectName() : "No Project Assigned",
                        (team.getProject() != null) ? team.getProject().getPid() : null,
                        team.getLeader().getFirstname() + " " + team.getLeader().getLastname(),
                        team.getClassRef().getCid(),
                        team.getMembers().stream().map(User::getUid).toList(),
                        team.isRecruitmentOpen(),
                        null,
                        (team.getProject() != null) ? team.getProject().getDescription() : "No Description Available",
                        (team.getAdviser() != null) ? team.getAdviser().getUid() : null,
                        (team.getSchedule() != null) ? team.getSchedule().getSchedid() : null,
                        team.getClassRef().getMaxTeamSize()
                ))
                .toList();
    }




    //non-member function
    @Transactional
    public void leaveTeam(int teamId, int userId) {
        Team team = tRepo.findById(teamId)
                .orElseThrow(() -> new NoSuchElementException("Team with ID " + teamId + " not found."));

        User user = uRepo.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with ID " + userId + " not found."));

        if (!team.getMembers().contains(user)) {
            throw new IllegalStateException("User is not a member of this team.");
        }

        if (team.getLeader().getUid() == userId) {
            throw new IllegalStateException("Leaders cannot leave their own team. Transfer leadership first.");
        }
        team.getMembers().remove(user);
        tRepo.save(team);
        trRepo.deleteByTeamIdAndStudentId(teamId, userId);
    }



    //all get functions
    public List<TeamDTO> getAllActiveTeams() {
        return tRepo.findAllActiveTeams().stream()
                .map(team -> new TeamDTO(
                        team.getTid(),
                        team.getGroupName(),
                        (team.getProject() != null) ? team.getProject().getProjectName() : "No Project Assigned",
                        (team.getProject() != null) ? team.getProject().getPid() : null,
                        team.getLeader().getFirstname() + " " + team.getLeader().getLastname(),
                        team.getClassRef().getCid(),
                        team.getMembers().stream().map(User::getUid).toList(),
                        team.isRecruitmentOpen(),
                        null,
                        (team.getProject() != null) ? team.getProject().getDescription() : "No Description Available",
                        (team.getAdviser() != null) ? team.getAdviser().getUid() : null,
                        (team.getSchedule() != null) ? team.getSchedule().getSchedid() : null
                ))
                .toList();
    }

    public TeamDTO getTeamById(int teamId) {
        Team team = tRepo.findActiveTeamById(teamId)
                .orElseThrow(() -> new NoSuchElementException("Team with ID " + teamId + " not found or has been deleted."));

        List<FeatureDTO> features = (team.getProject() != null) ?
                fRepo.findByProjectId(team.getProject().getPid()).stream()
                        .map(feature -> new FeatureDTO(feature.getFeatureTitle(), feature.getFeatureDescription()))
                        .toList()
                : new ArrayList<>();

        return new TeamDTO(
                team.getTid(),
                team.getGroupName(),
                (team.getProject() != null) ? team.getProject().getProjectName() : "No Project Assigned",
                (team.getProject() != null) ? team.getProject().getPid() : null,
                team.getLeader().getFirstname() + " " + team.getLeader().getLastname(),
                team.getClassRef().getCid(),
                team.getMembers().stream().map(User::getUid).toList(),
                team.isRecruitmentOpen(),
                features,
                (team.getProject() != null) ? team.getProject().getDescription() : "No Description Available",
                (team.getAdviser() != null) ? team.getAdviser().getUid() : null,
                (team.getSchedule() != null) ? team.getSchedule().getSchedid() : null
        );
    }

    //will customize
    public List<TeamDTO> getTeamsByClassId(int classId) {
        List<Team> activeTeams = tRepo.findActiveTeamsByClassId(classId);

        if (activeTeams.isEmpty()) {
            throw new NoSuchElementException("No active teams found for class ID " + classId);
        }

        List<TeamDTO> teamDTOs = new ArrayList<>();

        for (Team team : activeTeams) {
            String projectName = (team.getProject() != null) ? team.getProject().getProjectName() : "No Project Assigned";
            Integer projectId = (team.getProject() != null) ? team.getProject().getPid() : null;
            String projectDescription = (team.getProject() != null) ? team.getProject().getDescription() : "No Description Available";

            String leaderName = (team.getLeader() != null) ? team.getLeader().getFirstname() + " " + team.getLeader().getLastname() : "Unknown Leader";

            String adviserName = (team.getAdviser() != null) ? team.getAdviser().getFirstname() + " " + team.getAdviser().getLastname() : "No Adviser Assigned";

            String scheduleDay = (team.getSchedule() != null) ? team.getSchedule().getDay().name() : "No Day Set";

            String scheduleTime = (team.getSchedule() != null)
                    ? team.getSchedule().getStartTime().format(TIME_FORMATTER) + " - " + team.getSchedule().getEndTime().format(TIME_FORMATTER)
                    : "No Time Set";

            List<String> memberNames = team.getMembers().stream()
                    .filter(member -> team.getLeader() == null || member.getUid() != team.getLeader().getUid())
                    .map(member -> member.getFirstname() + " " + member.getLastname())
                    .collect(Collectors.toList());

            List<FeatureDTO> features = new ArrayList<>();
            if (team.getProject() != null) {
                features = fRepo.findByProjectId(projectId).stream()
                        .map(feature -> new FeatureDTO(feature.getFeatureTitle(), feature.getFeatureDescription()))
                        .toList();
            }

            teamDTOs.add(new TeamDTO(
                    team.getTid(),
                    team.getGroupName(),
                    projectName,
                    projectId,
                    leaderName,
                    team.getClassRef().getCid(),
                    team.getMembers().stream().map(User::getUid).toList(),
                    team.isRecruitmentOpen(),
                    features,
                    projectDescription,
                    adviserName,
                    scheduleDay,
                    scheduleTime,
                    memberNames
            ));
        }
        return teamDTOs;
    }

    public TeamDTO getMyTeam(int userId, int classId) {
        Team team = tRepo.findMyTeamByClassId(userId, classId)
                .orElseThrow(() -> new NoSuchElementException("No team found for the user in this class."));

        String projectName = (team.getProject() != null) ? team.getProject().getProjectName() : "No Project Assigned";
        Integer projectId = (team.getProject() != null) ? team.getProject().getPid() : null;
        String projectDescription = (team.getProject() != null) ? team.getProject().getDescription() : "No Description Available";
        String leaderName = (team.getLeader() != null) ? team.getLeader().getFirstname() + " " + team.getLeader().getLastname() : "Unknown Leader";

        String scheduleDay = (team.getSchedule() != null) ? team.getSchedule().getDay().name() : "No Day Set";

        String scheduleTime = (team.getSchedule() != null)
                ? team.getSchedule().getStartTime().format(TIME_FORMATTER) + " - " + team.getSchedule().getEndTime().format(TIME_FORMATTER)
                : "No Time Set";

        List<String> memberNames = team.getMembers().stream()
                .filter(member -> team.getLeader() == null || member.getUid() != team.getLeader().getUid())
                .map(member -> member.getFirstname() + " " + member.getLastname())
                .collect(Collectors.toList());

        return new TeamDTO(
                team.getTid(),
                team.getGroupName(),
                projectName,
                projectId,
                leaderName,
                team.getClassRef().getCid(),
                team.getMembers().stream().map(User::getUid).toList(),
                team.isRecruitmentOpen(),
                null,
                projectDescription,
                team.getAdviser() != null ? team.getAdviser().getFirstname() + " " + team.getAdviser().getLastname() : "No Adviser Assigned",
                scheduleDay,
                scheduleTime,
                memberNames
        );
    }

    public List<UserDTO> getTeamMembers(int teamId) {
        List<User> members = tRepo.findMembersByTeamId(teamId);
        return members.stream()
                .map(member -> new UserDTO(member.getFirstname(), member.getLastname()))
                .toList();
    }

    public List<UserDTO> getAvailableStudentsForTeam(Long classId, String searchTerm) {
        List<User> availableStudents = uRepo.findAvailableStudentsForTeamWithSearch(classId, searchTerm);
        return availableStudents.stream()
                .map(student -> new UserDTO(student.getFirstname(), student.getLastname(), student.getEmail(), student.getUid()))
                .collect(Collectors.toList());
    }

    public List<UserDTO> getQualifiedAdvisersForClass(Long classId) {
        List<User> advisers = cRepo.findQualifiedAdvisersByClassId(classId);

        return advisers.stream()
                .map(adviser -> new UserDTO(
                        adviser.getUid(),
                        adviser.getFirstname(),
                        adviser.getLastname(),
                        adviser.getEmail(),
                        adviser.getRole(),
                        adviser.getInterests(),
                        adviser.getDepartment()
                ))
                .collect(Collectors.toList());
    }

    public List<TeamDTO> getTeamsByAdviser(int adviserId) {
        List<Team> teams = tRepo.findTeamsByAdviserAndSchedule(adviserId);

        if (teams.isEmpty()) {
            throw new NoSuchElementException("No teams found for adviser ID " + adviserId);
        }

        List<TeamDTO> teamDTOs = new ArrayList<>();

        for (Team team : teams) {
            String projectName = (team.getProject() != null) ? team.getProject().getProjectName() : "No Project Assigned";
            Integer projectId = (team.getProject() != null) ? team.getProject().getPid() : null;
            String projectDescription = (team.getProject() != null) ? team.getProject().getDescription() : "No Description Available";
            String leaderName = (team.getLeader() != null) ? team.getLeader().getFirstname() + " " + team.getLeader().getLastname() : "Unknown Leader";
            String adviserName = (team.getAdviser() != null) ? team.getAdviser().getFirstname() + " " + team.getAdviser().getLastname() : "No Adviser Assigned";
            String scheduleDay = (team.getSchedule() != null) ? team.getSchedule().getDay().name() : "No Day Set";
            String scheduleTime = (team.getSchedule() != null)
                    ? team.getSchedule().getStartTime().format(TIME_FORMATTER) + " - " + team.getSchedule().getEndTime().format(TIME_FORMATTER)
                    : "No Time Set";

            String courseDescription = (team.getClassRef() != null) ? team.getClassRef().getCourseDescription() : "No Class Info Available";

            List<String> memberNames = team.getMembers().stream()
                    .filter(member -> team.getLeader() == null || member.getUid() != team.getLeader().getUid())
                    .map(member -> member.getFirstname() + " " + member.getLastname())
                    .collect(Collectors.toList());

            List<FeatureDTO> features = new ArrayList<>();
            if (team.getProject() != null) {
                features = fRepo.findByProjectId(projectId).stream()
                        .map(feature -> new FeatureDTO(feature.getFeatureTitle(), feature.getFeatureDescription()))
                        .toList();
            }
            teamDTOs.add(new TeamDTO(
                    team.getTid(),
                    team.getGroupName(),
                    projectName,
                    projectId,
                    leaderName,
                    team.getClassRef().getCid(),
                    team.getMembers().stream().map(User::getUid).toList(),
                    team.isRecruitmentOpen(),
                    features,
                    projectDescription,
                    adviserName,
                    scheduleDay,
                    scheduleTime,
                    courseDescription,
                    memberNames
            ));
        }
        return teamDTOs;
    }




    //Q it services (for maboi Jandel
    public List<TeamDTO> retrieveTeamsForMentor(int mentorID) {
        List<Team> retrievedTeams = tRepo.retrieveTeamsForMentor(mentorID);
        List<TeamDTO> teamsToReturn = new ArrayList<>();

        retrievedTeams.stream()
                .forEach(team -> {
                    List<String> memberNames = team.getMembers().stream()
                            .map(member -> member.getFirstname() + " " + member.getLastname())
                            .collect(Collectors.toList());
                    TeamDTO foo = new TeamDTO(
                        team.getTid(),
                        team.getGroupName(),
                        team.getClassRef().getCid(),
                        team.getMembers().stream().map(User::getUid).toList(),
                        memberNames,
                        team.getSchedule().getDay(),
                        team.getSchedule().getStartTime(),
                        team.getSchedule().getEndTime()
                    );
                    teamsToReturn.add(foo);
                });
        return teamsToReturn;
    }

    public List<TeamDTO> retrieveScheduledTeamsForMeetingAutomation(String concernedDay){
        DayOfWeek day = DayOfWeek.valueOf(concernedDay);
        List<Team> retrievedTeams = tRepo.retrieveScheduledTeamsForMeetingAutomation(day);
        List<TeamDTO> teamsToReturn = new ArrayList<>();

        retrievedTeams.stream()
                .forEach(team -> {
                    List<String> memberNames = team.getMembers().stream()
                            .map(member -> member.getFirstname() + " " + member.getLastname())
                            .collect(Collectors.toList());
                    TeamDTO foo = new TeamDTO(
                            team.getTid(),
                            team.getGroupName(),
                            team.getClassRef().getCid(),
                            team.getMembers().stream().map(User::getUid).toList(),
                            memberNames,
                            team.getSchedule().getDay(),
                            team.getSchedule().getStartTime(),
                            team.getSchedule().getEndTime(),
                            team.getAdviser().getUid()
                    );
                    teamsToReturn.add(foo);
                });
        return teamsToReturn;
    }
}
