package com.group2.SPEAR_Backend.Repository;

import com.group2.SPEAR_Backend.Model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Integer> {

    @Query("SELECT t FROM Team t WHERE t.isDeleted = false")
    List<Team> findAllActiveTeams();

    @Query("SELECT t FROM Team t WHERE t.isDeleted = false AND t.members.size < 5")
    List<Team> findIncompleteTeams();

    @Query("SELECT t FROM Team t WHERE t.isDeleted = false AND t.members.size = 5")
    List<Team> findCompleteTeams();

    @Query("SELECT t FROM Team t WHERE t.classRef.cid = :classId AND t.isDeleted = false")
    List<Team> findTeamsByClassId(@Param("classId") int classId);

    @Query("SELECT t FROM Team t WHERE t.isRecruitmentOpen = true AND t.isDeleted = false")
    List<Team> findOpenRecruitmentTeams();
}
