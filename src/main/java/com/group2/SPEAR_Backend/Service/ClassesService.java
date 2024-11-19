package com.group2.SPEAR_Backend.Service;

import com.group2.SPEAR_Backend.ClassCodeGenerator;
import com.group2.SPEAR_Backend.Model.Classes;
import com.group2.SPEAR_Backend.Model.User;
import com.group2.SPEAR_Backend.Repository.ClassesRepository;
import com.group2.SPEAR_Backend.DTO.ClassesDTO;
import com.group2.SPEAR_Backend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassesService {

    private final ClassesRepository cRepo;

    @Autowired
    public ClassesService(ClassesRepository cRepo) {
        this.cRepo = cRepo;
    }

    @Autowired
    UserRepository uRepo;

    public ClassesDTO createClass(ClassesDTO classRequest) {
        ClassesDTO response = new ClassesDTO();
        try {
            User user = classRequest.getCreatedBy();
            Optional<User> optionalUser = uRepo.findById(user.getUid());

            if (optionalUser.isEmpty()) {
                response.setStatusCode(404);
                response.setMessage("User not found");
                return response;
            }
            User createdby = optionalUser.get();

            // code generator
            String generatedClassKey = ClassCodeGenerator.generateClassCode();

            Classes newClass = new Classes();
            newClass.setCourseType(classRequest.getCourseType());
            newClass.setCourseCode(classRequest.getCourseCode());
            newClass.setSection(classRequest.getSection());
            newClass.setSchoolYear(classRequest.getSchoolYear());
            newClass.setSemester(classRequest.getSemester());
            newClass.setCourseDescription(classRequest.getCourseDescription());
            newClass.setCourseName(classRequest.getCourseName());
            newClass.setClassKey(generatedClassKey);
            newClass.setCreatedBy(createdby);
            newClass.setIsDeleted(false);
            Classes savedClass = cRepo.save(newClass);

            if (savedClass.getCid() != null) {
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



    public ClassesDTO getAllClasses() {
        ClassesDTO response = new ClassesDTO();
        try {
            List<Classes> classesList = cRepo.findAllByIsDeletedFalse();
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
            Classes classByCourseCode = cRepo.findByCourseCode(courseCode);
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
            Optional<Classes> classOptional = cRepo.findById(classId);
            if (classOptional.isPresent()) {
                Classes existingClass = classOptional.get();
                existingClass.setCourseType(classRequest.getCourseType());
                existingClass.setCourseCode(classRequest.getCourseCode());
                existingClass.setSection(classRequest.getSection());
                existingClass.setSchoolYear(classRequest.getSchoolYear());
                existingClass.setSemester(classRequest.getSemester());
                existingClass.setCourseDescription(classRequest.getCourseDescription());

                Classes updatedClass = cRepo.save(existingClass);
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
            Optional<Classes> classOptional = cRepo.findById(classId);
            if (classOptional.isPresent()) {
                Classes classToDelete = classOptional.get();
                if (!classToDelete.getIsDeleted()) {
                    classToDelete.setIsDeleted(true);
                    cRepo.save(classToDelete);
                    response.setStatusCode(200);
                    response.setMessage("Class deleted successfully");
                } else {
                    response.setStatusCode(400);
                    response.setMessage("Class deleted successfully");
                }
            } else {
                response.setStatusCode(404);
                response.setMessage("Class with ID '" + classId + "' not found");
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred: " + e.getMessage());
        }
        return response;
    }

    public ClassesDTO getClassKeyByCourseCode(String courseCode) {
        ClassesDTO response = new ClassesDTO();
        try {
            Classes classByCourseCode = cRepo.findByCourseCode(courseCode);
            if (classByCourseCode != null) {
                response.setClassKey(classByCourseCode.getClassKey());
                response.setStatusCode(200);
                response.setMessage("ClassKey for course code '" + courseCode + "' retrieved successfully.");
            } else {
                response.setStatusCode(404);
                response.setMessage("No class found with course code: " + courseCode);
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError(e.getMessage());
            response.setMessage("Error occurred while fetching classKey by course code.");
        }
        return response;
    }

}
