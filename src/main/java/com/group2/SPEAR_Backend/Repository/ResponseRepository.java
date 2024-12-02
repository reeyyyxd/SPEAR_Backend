package com.group2.SPEAR_Backend.Repository;

import com.group2.SPEAR_Backend.Model.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {
    List<Response> findByEvaluatorUid(int evaluatorId);
    List<Response> findByEvaluateeUid(int evaluateeId);
}
