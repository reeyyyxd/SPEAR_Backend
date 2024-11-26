package com.group2.SPEAR_Backend.Repository;

import com.group2.SPEAR_Backend.Model.ProjectProposal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectProposalRepository extends JpaRepository<ProjectProposal, Integer> {

    @Query("SELECT p FROM ProjectProposal p WHERE p.isDeleted = false")
    List<ProjectProposal> findAllActive();

    @Query("SELECT p FROM ProjectProposal p WHERE p.classProposal.cid = :classId AND p.isDeleted = false")
    List<ProjectProposal> findByClassIdNotDeleted(@Param("classId") Long classId);
}
