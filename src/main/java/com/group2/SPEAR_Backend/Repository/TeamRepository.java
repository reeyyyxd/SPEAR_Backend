package com.group2.SPEAR_Backend.Repository;

import com.group2.SPEAR_Backend.Entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<TeamEntity, Integer> {
}
