package com.group2.SPEAR_Backend.Service;

import com.group2.SPEAR_Backend.DTO.TeamRecuitmentDTO;
import com.group2.SPEAR_Backend.Model.Team;
import com.group2.SPEAR_Backend.Model.TeamRecuitment;
import com.group2.SPEAR_Backend.Model.User;
import com.group2.SPEAR_Backend.Repository.TeamRecuitmentRepository;
import com.group2.SPEAR_Backend.Repository.TeamRepository;
import com.group2.SPEAR_Backend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TeamRecuitmentService {

    @Autowired
    private TeamRecuitmentRepository trRepo;

    @Autowired
    private TeamRepository tRepo;

    @Autowired
    private UserRepository uRepo;


    @Transactional
    public TeamRecuitment applyToTeam(int teamId, int studentId, String role, String reason) {
        // Get the team
        Team team = tRepo.findById(teamId)
                .orElseThrow(() -> new NoSuchElementException("Team with ID " + teamId + " not found"));

        // Get the student
        User student = uRepo.findById(studentId)
                .orElseThrow(() -> new NoSuchElementException("Student with ID " + studentId + " not found"));

        // Ensure the student is enrolled in the class
        if (!team.getClassRef().getEnrolledStudents().contains(student)) {
            throw new IllegalStateException("Student must be enrolled in the class to apply for a team.");
        }

        // Check if the student already has a pending or accepted application
        Optional<TeamRecuitment> existingApplication = trRepo.findByTeamIdAndStudentId(teamId, studentId);
        if (existingApplication.isPresent()) {
            TeamRecuitment existing = existingApplication.get();
            if (existing.getStatus() == TeamRecuitment.Status.PENDING ||
                    existing.getStatus() == TeamRecuitment.Status.ACCEPTED) {
                throw new IllegalStateException("You already have a pending or accepted application for this team.");
            }
            // If rejected, allow re-application by resetting to pending
            existing.setStatus(TeamRecuitment.Status.PENDING);
            existing.setRole(role);
            existing.setReason(reason);
            return trRepo.save(existing);
        }

        // Create new recruitment request
        TeamRecuitment recruitment = new TeamRecuitment(team, student, role, reason, TeamRecuitment.Status.PENDING);
        return trRepo.save(recruitment);
    }

    @Transactional
    public void reviewApplication(int recruitmentId, boolean isAccepted, String leaderReason) {
        // Find recruitment request
        TeamRecuitment recruitment = trRepo.findById(recruitmentId)
                .orElseThrow(() -> new NoSuchElementException("Recruitment request not found"));

        Team team = recruitment.getTeam();

        if (isAccepted) {
            // Check if team has reached its max size
            if (team.getMembers().size() >= team.getClassRef().getMaxTeamSize()) {
                throw new IllegalStateException("Team is already full.");
            }

            recruitment.setStatus(TeamRecuitment.Status.ACCEPTED);
            recruitment.setReason(null); // Clear reason since it's accepted
            team.getMembers().add(recruitment.getStudent()); // Add student to team
            tRepo.save(team);
        } else {
            recruitment.setStatus(TeamRecuitment.Status.REJECTED);
            recruitment.setReason(leaderReason != null ? leaderReason : "No reason provided.");
        }

        trRepo.save(recruitment);
    }

    public List<TeamRecuitmentDTO> getPendingApplicationsByTeam(int teamId) {
        return trRepo.findPendingByTeamId(teamId).stream()
                .map(recruitment -> new TeamRecuitmentDTO(
                        recruitment.getTrid(),
                        recruitment.getTeam().getTid(),
                        recruitment.getStudent().getUid(),
                        recruitment.getStudent().getFirstname() + " " + recruitment.getStudent().getLastname(),
                        recruitment.getRole(),
                        recruitment.getReason(),
                        recruitment.getStatus()
                ))
                .toList();
    }
}
