package com.group2.SPEAR_Backend.Repository;

import com.group2.SPEAR_Backend.DTO.TeamDTO;
import com.group2.SPEAR_Backend.Model.Team;
import com.group2.SPEAR_Backend.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Integer> {

    @Query("SELECT t FROM Team t WHERE t.isDeleted = false")
    List<Team> findAllActiveTeams();

//    @Query("SELECT t FROM Team t WHERE SIZE(t.members) < 4 AND t.isDeleted = false")
//    List<Team> findIncompleteTeams();
//
//    @Query("SELECT t FROM Team t WHERE SIZE(t.members) = 4 AND t.isDeleted = false")
//    List<Team> findCompleteTeams();
//
//    @Query("SELECT t FROM Team t WHERE t.isRecruitmentOpen = true AND t.isDeleted = false")
//    List<Team> findOpenRecruitmentTeams();
//
//    @Query("SELECT t FROM Team t WHERE t.isRecruitmentOpen = false AND t.isDeleted = false")
//    List<Team> findCloseRecruitmentTeams();
//
//    @Query("SELECT t FROM Team t WHERE t.classRef.cid = :classId AND t.isDeleted = false")
//    List<Team> findTeamsByClassId(@Param("classId") int classId);

    @Query("SELECT new com.group2.SPEAR_Backend.DTO.TeamDTO(t.tid, t.groupName, t.project.projectName, t.isRecruitmentOpen) " +
            "FROM Team t " +
            "WHERE t.classRef.cid = :classId AND t.isDeleted = false")
    List<TeamDTO> findTeamsByClassIdWithDetails(@Param("classId") int classId);

    @Query("SELECT t FROM Team t " +
            "WHERE (t.leader.uid = :userId OR :userId IN (SELECT m.uid FROM t.members m)) " +
            "AND t.classRef.cid = :classId AND t.isDeleted = false")
    Optional<Team> findMyTeamByClassId(@Param("userId") int userId, @Param("classId") int classId);

    @Query("SELECT m FROM Team t JOIN t.members m WHERE t.tid = :teamId")
    List<User> findMembersByTeamId(@Param("teamId") int teamId);

    List<Team> findAllByMembersContains(User user);

    @Query("SELECT t FROM Team t JOIN t.members m WHERE m.uid = :userId AND t.isDeleted = false")
    List<Team> findActiveTeamsByMemberId(@Param("userId") int userId);

    @Query("UPDATE Team t SET t.isDeleted = true WHERE t.leader.uid = :leaderId AND t.isDeleted = false")
    void softDeleteTeamsByLeaderId(@Param("leaderId") int leaderId);

    @Query("SELECT t FROM Team t WHERE t.tid = :teamId AND t.isDeleted = false")
    Optional<Team> findActiveTeamById(@Param("teamId") int teamId);

    @Query("SELECT t FROM Team t WHERE t.classRef.cid = :classId AND t.isDeleted = false")
    List<Team> findActiveTeamsByClassId(@Param("classId") int classId);

    @Query("SELECT t FROM Team t WHERE t.leader.uid = :leaderId AND t.isDeleted = false")
    List<Team> findActiveTeamsByLeaderId(@Param("leaderId") int leaderId);


}
