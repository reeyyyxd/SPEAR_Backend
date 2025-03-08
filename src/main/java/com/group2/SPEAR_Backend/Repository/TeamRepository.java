package com.group2.SPEAR_Backend.Repository;

import com.group2.SPEAR_Backend.DTO.TeamDTO;
import com.group2.SPEAR_Backend.Model.Classes;
import com.group2.SPEAR_Backend.Model.Schedule;
import com.group2.SPEAR_Backend.Model.Team;
import com.group2.SPEAR_Backend.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Integer> {

    @Query("SELECT t FROM Team t")
    List<Team> findAllActiveTeams();

    @Query("SELECT new com.group2.SPEAR_Backend.DTO.TeamDTO(t.tid, t.groupName, t.project.projectName, t.isRecruitmentOpen) " +
            "FROM Team t " +
            "WHERE t.classRef.cid = :classId")
    List<TeamDTO> findTeamsByClassIdWithDetails(@Param("classId") int classId);

    @Query("SELECT t FROM Team t " +
            "WHERE (t.leader.uid = :userId OR :userId IN (SELECT m.uid FROM t.members m)) " +
            "AND t.classRef.cid = :classId")
    Optional<Team> findMyTeamByClassId(@Param("userId") int userId, @Param("classId") int classId);

    @Query("SELECT m FROM Team t JOIN t.members m WHERE t.tid = :teamId")
    List<User> findMembersByTeamId(@Param("teamId") int teamId);

    List<Team> findAllByMembersContains(User user);

    @Query("SELECT t FROM Team t JOIN t.members m WHERE m.uid = :userId")
    List<Team> findActiveTeamsByMemberId(@Param("userId") int userId);

    @Query("SELECT t FROM Team t WHERE t.tid = :teamId")
    Optional<Team> findActiveTeamById(@Param("teamId") int teamId);

    @Query("SELECT t FROM Team t WHERE t.classRef.cid = :classId")
    List<Team> findActiveTeamsByClassId(@Param("classId") int classId);

    @Query("SELECT t FROM Team t WHERE t.leader.uid = :leaderId")
    List<Team> findActiveTeamsByLeaderId(@Param("leaderId") int leaderId);

    @Query("SELECT COUNT(t) > 0 FROM Team t WHERE t.leader = :leader AND t.classRef = :classRef")
    boolean existsByLeaderAndClassRef(@Param("leader") User leader, @Param("classRef") Classes classRef);

    @Query("SELECT t FROM Team t WHERE t.schedule = :schedule")
    Optional<Team> findBySchedule(@Param("schedule") Schedule schedule);


    @Modifying
    @Query(value = "DELETE FROM team_members WHERE team_id = :teamId", nativeQuery = true)
    void deleteTeamMembers(@Param("teamId") int teamId);









}