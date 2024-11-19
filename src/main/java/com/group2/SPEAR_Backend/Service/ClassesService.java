package com.group2.SPEAR_Backend.Service;

import com.group2.SPEAR_Backend.Model.Classes;
import com.group2.SPEAR_Backend.Repository.ClassesRepository;
import com.group2.SPEAR_Backend.DTO.ClassesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassesService {

    private final ClassesRepository classesRepository;

    @Autowired
    public ClassesService(ClassesRepository classesRepository) {
        this.classesRepository = classesRepository;
    }

    // Create a new class
    public ClassesDTO createClass(ClassesDTO classRequest) {
        ClassesDTO response = new ClassesDTO();
        try {
            Classes newClass = new Classes();
            newClass.setCourseType(classRequest.getCourseType());
            newClass.setCourseCode(classRequest.getCourseCode());
            newClass.setSection(classRequest.getSection());
            newClass.setSchoolYear(classRequest.getSchoolYear());
            newClass.setSemester(classRequest.getSemester());
            newClass.setCourseDescription(classRequest.getCourseDescription());

            Classes savedClass = classesRepository.save(newClass);
            if (savedClass.getId() != null) {
                response.setClasses(savedClass);
                response.setStatusCode(200);
                response.setMessage("Class created successfully");
            }

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError(e.getMessage());
            response.setMessage("Error occurred while creating class");
        }
        return response;
    }

    // Get all classes
    public ClassesDTO getAllClasses() {
        ClassesDTO response = new ClassesDTO();
        try {
            List<Classes> classesList = classesRepository.findAll();
            if (!classesList.isEmpty()) {
                response.setClassesList(classesList);
                response.setStatusCode(200);
                response.setMessage("Classes retrieved successfully");
            } else {
                response.setStatusCode(404);
                response.setMessage("No classes found");
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while fetching classes: " + e.getMessage());
        }
        return response;
    }

    // Get a class by course code
    public ClassesDTO getClassByCourseCode(String courseCode) {
        ClassesDTO response = new ClassesDTO();
        try {
            Classes classByCourseCode = classesRepository.findByCourseCode(courseCode);
            if (classByCourseCode != null) {
                response.setClasses(classByCourseCode);
                response.setStatusCode(200);
                response.setMessage("Class with course code '" + courseCode + "' found successfully");
            } else {
                response.setStatusCode(404);
                response.setMessage("No class found with course code: " + courseCode);
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while fetching class by course code: " + e.getMessage());
        }
        return response;
    }

    // Update a class
    public ClassesDTO updateClass(Long classId, ClassesDTO classRequest) {
        ClassesDTO response = new ClassesDTO();
        try {
            Optional<Classes> classOptional = classesRepository.findById(classId);
            if (classOptional.isPresent()) {
                Classes existingClass = classOptional.get();
                existingClass.setCourseType(classRequest.getCourseType());
                existingClass.setCourseCode(classRequest.getCourseCode());
                existingClass.setSection(classRequest.getSection());
                existingClass.setSchoolYear(classRequest.getSchoolYear());
                existingClass.setSemester(classRequest.getSemester());
                existingClass.setCourseDescription(classRequest.getCourseDescription());

                Classes updatedClass = classesRepository.save(existingClass);
                response.setClasses(updatedClass);
                response.setStatusCode(200);
                response.setMessage("Class updated successfully");
            } else {
                response.setStatusCode(404);
                response.setMessage("Class with ID '" + classId + "' not found");
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while updating class: " + e.getMessage());
        }
        return response;
    }

    // Delete a class
    public ClassesDTO deleteClass(Long classId) {
        ClassesDTO response = new ClassesDTO();
        try {
            Optional<Classes> classOptional = classesRepository.findById(classId);
            if (classOptional.isPresent()) {
                classesRepository.deleteById(classId);
                response.setStatusCode(200);
                response.setMessage("Class deleted successfully");
            } else {
                response.setStatusCode(404);
                response.setMessage("Class with ID '" + classId + "' not found");
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while deleting class: " + e.getMessage());
        }
        return response;
    }
}
