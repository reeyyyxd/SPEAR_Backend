package com.group2.SPEAR_Backend.Service;

import com.group2.SPEAR_Backend.Entity.ClassesEntity;
import com.group2.SPEAR_Backend.Repository.ClassesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ClassesService {

    @Autowired
    ClassesRepository classesRepository;

    // Create class
    public ClassesEntity createClass(ClassesEntity newClass) {
        return classesRepository.save(newClass);
    }

    // Get all classes
    public List<ClassesEntity> getAllClasses() {
        return classesRepository.findAll();
    }

    // Update class
    public ClassesEntity updateClass(int id, ClassesEntity updatedClass) {
        try {
            ClassesEntity existingClass = classesRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Class with id " + id + " not found"));

            existingClass.setClassName(updatedClass.getClassName());
            existingClass.setClassCode(updatedClass.getClassCode());
            existingClass.setSection(updatedClass.getSection());
            existingClass.setSchoolYear(updatedClass.getSchoolYear());
            existingClass.setSemester(updatedClass.getSemester());
            existingClass.setClassDescription(updatedClass.getClassDescription());

            return classesRepository.save(existingClass);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Class with id " + id + " not found");
        }
    }

    // Delete class
    public String deleteClass(int id) {
        if (classesRepository.existsById(id)) {
            classesRepository.deleteById(id);
            return "Class with id " + id + " deleted";
        } else {
            return "Class with id " + id + " not found";
        }
    }

    // Find class by name
    public ClassesEntity findClassByName(String className) {
        return classesRepository.findByClassName(className);
    }
}
