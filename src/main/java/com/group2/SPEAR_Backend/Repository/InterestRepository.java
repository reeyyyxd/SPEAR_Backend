package com.group2.SPEAR_Backend.Repository;

import com.group2.SPEAR_Backend.Model.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Integer> {
    Optional<Interest> findByUserInterest_Uid(int userId);
}
