package com.group2.SPEAR_Backend.Service;

import com.group2.SPEAR_Backend.DTO.TeamRecuitmentDTO;
import com.group2.SPEAR_Backend.Model.Team;
import com.group2.SPEAR_Backend.Model.TeamRecuitment;
import com.group2.SPEAR_Backend.Repository.TeamRecuitmentRepository;
import com.group2.SPEAR_Backend.Repository.TeamRepository;
import com.group2.SPEAR_Backend.Model.User;
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
    public TeamRecuitment applyToTeam(int teamId, User student, String role, String reason) {
        Team team = tRepo.findById(teamId)
                .orElseThrow(() -> new NoSuchElementException("Team with ID " + teamId + " not found"));

        List<TeamRecuitment> existingApplications = trRepo.findByStudentId(student.getUid());
        if (existingApplications.stream().anyMatch(r -> r.getTeam().getTid() == teamId &&
                (r.getStatus() == TeamRecuitment.Status.PENDING ||
                        r.getStatus() == TeamRecuitment.Status.ACCEPTED))) {
            throw new IllegalStateException("You already have a pending or accepted application for this team.");
        }

        Optional<TeamRecuitment> rejectedApplication = trRepo.findByTeamIdAndStudentId(teamId, student.getUid());
        if (rejectedApplication.isPresent() && rejectedApplication.get().getStatus() == TeamRecuitment.Status.REJECTED) {
            TeamRecuitment recruitment = rejectedApplication.get();
            recruitment.setStatus(TeamRecuitment.Status.PENDING);
            recruitment.setReason(reason);
            recruitment.setRole(role);
            return trRepo.save(recruitment);
        }

        TeamRecuitment recruitment = new TeamRecuitment(team, student, role, reason, TeamRecuitment.Status.PENDING);
        return trRepo.save(recruitment);
    }

    @Transactional
    public void reviewApplication(int recruitmentId, boolean isAccepted, String leaderReason) {
        TeamRecuitment recruitment = trRepo.findById(recruitmentId)
                .orElseThrow(() -> new NoSuchElementException("Recruitment request not found"));

        Team team = recruitment.getTeam();

        if (isAccepted) {
            if (team.getMembers().size() >= 4) {
                throw new IllegalStateException("Team is already full.");
            }

            recruitment.setStatus(TeamRecuitment.Status.ACCEPTED);
            //clear for a obvious reason
            recruitment.setReason(null);
            team.getMembers().add(recruitment.getStudent());
            tRepo.save(team);
        } else {
            recruitment.setStatus(TeamRecuitment.Status.REJECTED);
            recruitment.setReason(leaderReason != null ? leaderReason : "No reason provided.");
        }

        trRepo.save(recruitment);
    }


    public List<TeamRecuitmentDTO> getPendingApplicationsByTeam(int teamId) {
        return trRepo.findPendingByTeamId(teamId).stream()
                .map(recruitment -> {
                    String studentName = uRepo.findFullNameById(recruitment.getStudent().getUid());
                    return new TeamRecuitmentDTO(
                            recruitment.getTrid(),
                            recruitment.getTeam().getTid(),
                            recruitment.getStudent().getUid(),
                            studentName,
                            recruitment.getRole(),
                            recruitment.getReason(),
                            recruitment.getStatus()
                    );
                })
                .toList();
    }



}
