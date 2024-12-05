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

    // Fetch project proposals and features from a specific class and student
    @Query("SELECT p FROM ProjectProposal p WHERE p.classProposal.cid = :classId AND p.proposedBy.uid = :studentId AND p.isDeleted = false")
    List<ProjectProposal> findByClassAndStudent(@Param("classId") Long classId, @Param("studentId") int studentId);

    // Fetch project proposals and features by adviser
    @Query("SELECT p FROM ProjectProposal p WHERE p.adviser.uid = :adviserId AND p.isDeleted = false")
    List<ProjectProposal> findByAdviser(@Param("adviserId") int adviserId);

    // Fetch all projects by status
    @Query("SELECT p FROM ProjectProposal p WHERE p.status = :status AND p.isDeleted = false")
    List<ProjectProposal> findByStatus(@Param("status") String status);

    @Query("SELECT p FROM ProjectProposal p WHERE p.classProposal.cid = :classId AND p.status = :status AND p.isDeleted = false")
    List<ProjectProposal> findByClassAndStatus(@Param("classId") Long classId, @Param("status") String status);

    @Query("SELECT CONCAT(u.firstname, ' ', u.lastname) FROM ProjectProposal p JOIN p.adviser u WHERE p.pid = :proposalId")
    String findAdviserFullNameByProposalId(@Param("proposalId") int proposalId);



}
