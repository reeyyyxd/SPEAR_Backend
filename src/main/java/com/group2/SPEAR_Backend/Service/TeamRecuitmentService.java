package com.group2.SPEAR_Backend.Service;

import com.group2.SPEAR_Backend.Model.Team;
import com.group2.SPEAR_Backend.Model.TeamRecuitment;
import com.group2.SPEAR_Backend.Repository.TeamRecuitmentRepository;
import com.group2.SPEAR_Backend.Repository.TeamRepository;
import com.group2.SPEAR_Backend.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TeamRecuitmentService {

    @Autowired
    private TeamRecuitmentRepository trRepo;

    @Autowired
    private TeamRepository tRepo;

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

        TeamRecuitment recruitment = new TeamRecuitment();
        recruitment.setTeam(team);
        recruitment.setStudent(student);
        recruitment.setRole(role);
        recruitment.setReason(reason);
        recruitment.setStatus(TeamRecuitment.Status.PENDING);

        return trRepo.save(recruitment);
    }

    @Transactional
    public void reviewApplication(int recruitmentId, boolean isAccepted, String leaderReason) {
        TeamRecuitment recruitment = trRepo.findById(recruitmentId)
                .orElseThrow(() -> new NoSuchElementException("Recruitment request not found"));

        Team team = recruitment.getTeam();

        if (isAccepted) {
            if (team.getMembers().size() >= 5) {
                throw new IllegalStateException("Team is already full.");
            }
            recruitment.setStatus(TeamRecuitment.Status.ACCEPTED);
            team.getMembers().add(recruitment.getStudent());
            tRepo.save(team);
        } else {
            recruitment.setStatus(TeamRecuitment.Status.REJECTED);
        }

        // optional nga ngano
        recruitment.setReason(leaderReason);

        trRepo.save(recruitment);
    }


    public List<TeamRecuitment> getPendingApplicationsByTeam(int teamId) {
        return trRepo.findPendingByTeamId(teamId);
    }
}
