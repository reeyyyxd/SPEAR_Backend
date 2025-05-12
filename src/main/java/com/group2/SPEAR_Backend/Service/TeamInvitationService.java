package com.group2.SPEAR_Backend.Service;

import com.group2.SPEAR_Backend.DTO.TeamInvitationDTO;
import com.group2.SPEAR_Backend.Model.*;
import com.group2.SPEAR_Backend.Repository.TeamInvitationRepository;
import com.group2.SPEAR_Backend.Repository.TeamRepository;
import com.group2.SPEAR_Backend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamInvitationService {

    @Autowired
    private TeamInvitationRepository invitationRepo;

    @Autowired
    private TeamRepository teamRepo;

    @Autowired
    private UserRepository userRepo;

    @Transactional
    public void inviteStudent(int teamId, int leaderId, int studentId) {
        Team team = teamRepo.findById(teamId)
                .orElseThrow(() -> new NoSuchElementException("Team not found"));

        if (!team.isRecruitmentOpen()) {
            throw new IllegalStateException("Team is no longer accepting members.");
        }

        if (team.getLeader().getUid() != leaderId) {
            throw new IllegalStateException("Only the team leader can invite members.");
        }

        User student = userRepo.findById(studentId)
                .orElseThrow(() -> new NoSuchElementException("Student not found"));

        if (!team.getClassRef().getEnrolledStudents().contains(student)) {
            throw new IllegalStateException("Student must be enrolled in the same class.");
        }

        if (teamRepo.findTeamByStudentAndClass((long) studentId, team.getClassRef().getCid()) != null) {
            throw new IllegalStateException("Student is already in a team for this class.");
        }

        // Count current members + pending invitations
        int currentMembers = team.getMembers().size(); // excluding leader
        int pendingInvites = invitationRepo.findByTeamAndStatus(team, TeamInvitation.Status.PENDING).size();
        int totalSize = currentMembers + pendingInvites + 1; // +1 for leader

        if (totalSize >= team.getClassRef().getMaxTeamSize()) {
            throw new IllegalStateException("Team already has enough members including pending invitations.");
        }

        Optional<TeamInvitation> existing = invitationRepo.findByTeamIdAndStudentId(teamId, studentId);
        if (existing.isPresent() && existing.get().getStatus() == TeamInvitation.Status.PENDING) {
            throw new IllegalStateException("Invitation already sent and is pending.");
        }

        TeamInvitation invite = new TeamInvitation(team, student, TeamInvitation.Status.PENDING);
        invitationRepo.save(invite);
    }

    @Transactional
    public void acceptInvitation(int invitationId) {
        TeamInvitation invitation = invitationRepo.findById(invitationId)
                .orElseThrow(() -> new NoSuchElementException("Invitation not found"));

        Team team   = invitation.getTeam();
        User student = invitation.getStudent();

        // make sure they’re not already on a team
        if (teamRepo.findTeamByStudentAndClass((long)student.getUid(), team.getClassRef().getCid()) != null
                || teamRepo.existsByLeaderUid(student.getUid())) {
            throw new IllegalStateException("Student is already in a team or is a team leader.");
        }

        // add member
        team.getMembers().add(student);
        invitation.setStatus(TeamInvitation.Status.ACCEPTED);

        // if we’ve now reached max size (leader + members), close recruitment
        int maxSize = team.getClassRef().getMaxTeamSize();
        if (team.getMembers().size() >= maxSize) {
            team.setRecruitmentOpen(false);
        }

        // persist both
        teamRepo.save(team);
        invitationRepo.deleteById(invitationId);
    }

    @Transactional
    public void rejectInvitation(int invitationId) {
        if (!invitationRepo.existsById(invitationId)) {
            throw new NoSuchElementException("Invitation not found");
        }
        invitationRepo.deleteById(invitationId);
    }

    public List<TeamInvitationDTO> getPendingInvitationsByStudent(int studentId) {
        List<TeamInvitation> invites = invitationRepo.findPendingInvitationsByStudent(studentId);

        return invites.stream()
                .map(i -> {
                    var team = i.getTeam();
                    var leader = team.getLeader();

                    List<String> members = team.getMembers().stream()
                            .filter(m -> m.getUid() != leader.getUid())
                            .map(m -> m.getFirstname() + " " + m.getLastname())
                            .collect(Collectors.toList());

                    return new TeamInvitationDTO(
                            i.getInvitationId(),
                            team.getTid(),
                            i.getStudent().getUid(),
                            i.getStudent().getFirstname() + " " + i.getStudent().getLastname(),
                            team.getGroupName(),
                            team.getClassRef().getCourseDescription(),
                            leader.getFirstname() + " " + leader.getLastname(),
                            members,
                            i.getStatus()
                    );
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteInvitation(int invitationId) {
        TeamInvitation inv = invitationRepo.findById(invitationId)
                .orElseThrow(() -> new NoSuchElementException("Invitation not found"));
        invitationRepo.delete(inv);
    }
}
