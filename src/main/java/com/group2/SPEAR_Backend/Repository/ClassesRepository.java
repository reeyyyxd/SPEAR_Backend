package com.group2.SPEAR_Backend.Repository;

import com.group2.SPEAR_Backend.Entity.ClassesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassesRepository extends JpaRepository <ClassesEntity, Integer> {
    ClassesEntity findByClassName(String className);
}
