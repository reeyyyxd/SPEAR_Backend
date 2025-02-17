package com.group2.SPEAR_Backend.Service;

import com.group2.SPEAR_Backend.ClassCodeGenerator;
import com.group2.SPEAR_Backend.DTO.UserDTO;
import com.group2.SPEAR_Backend.Model.Classes;
import com.group2.SPEAR_Backend.Model.ProjectProposal;
import com.group2.SPEAR_Backend.Model.Team;
import com.group2.SPEAR_Backend.Model.User;
import com.group2.SPEAR_Backend.Repository.ClassesRepository;
import com.group2.SPEAR_Backend.DTO.ClassesDTO;
import com.group2.SPEAR_Backend.Repository.ProjectProposalRepository;
import com.group2.SPEAR_Backend.Repository.TeamRepository;
import com.group2.SPEAR_Backend.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
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

    @Autowired
    TeamRepository tRepo;

    @Autowired
    ProjectProposalRepository ppRepo;


    public ClassesDTO createClass(ClassesDTO classRequest) {
        ClassesDTO response;
        try {
            Optional<User> optionalUser = uRepo.findById(classRequest.getCreatedBy().getUid());
            if (optionalUser.isEmpty()) {
                return new ClassesDTO(404, "User not found", (List<Classes>) null);
            }
            User createdBy = optionalUser.get();

            if (!"TEACHER".equalsIgnoreCase(createdBy.getRole())) {
                return new ClassesDTO(403, "Only users with the TEACHER role can create a class", (List<Classes>) null);
            }

            String generatedClassKey = ClassCodeGenerator.generateClassCode();
            int maxTeamSize = classRequest.getMaxTeamSize() > 0 ? classRequest.getMaxTeamSize() : 5;

            Classes newClass = new Classes(
                    createdBy,
                    classRequest.getCourseCode(),
                    classRequest.getSection(),
                    classRequest.getSchoolYear(),
                    classRequest.getSemester(),
                    generatedClassKey,
                    classRequest.getCourseDescription(),
                    false,
                    maxTeamSize
            );

            Classes savedClass = cRepo.save(newClass);

            // Build the response DTO correctly
            response = new ClassesDTO(
                    savedClass.getCid(),
                    savedClass.getClassKey(),
                    savedClass.getCourseCode(),
                    savedClass.getCourseDescription(),
                    savedClass.getSchoolYear(),
                    savedClass.getSection(),
                    savedClass.getSemester(),
                    savedClass.getCreatedBy().getFirstname(),
                    savedClass.getCreatedBy().getLastname(),
                    savedClass.getCreatedBy().getRole(),
                    savedClass.getMaxTeamSize()
            );

            response.setStatusCode(200);
            response.setMessage("Class created successfully");
        } catch (Exception e) {
            response = new ClassesDTO(500, "Error occurred while creating class", e.getMessage());
        }
        return response;
    }



    //super mortal sin (figure this one later)
    //UPDATE: one semester later pa na solve (yucks rey)
    public List<ClassesDTO> getAllClasses() {
        try {
            List<ClassesDTO> classesList = cRepo.findAllClassesWithCreator();
            if (!classesList.isEmpty()) {
                return classesList;
            } else {
                throw new NoSuchElementException("No classes found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while fetching classes: " + e.getMessage());
        }
    }


    public ClassesDTO getClassById(Long classId) {
        Optional<Classes> classOptional = cRepo.findActiveClassById(classId);

        if (classOptional.isEmpty()) {
            throw new NoSuchElementException("Class with ID " + classId + " not found or is deleted.");
        }

        Classes classData = classOptional.get();

        return new ClassesDTO(
                classData.getCid(),
                classData.getClassKey(),
                classData.getCourseCode(),
                classData.getCourseDescription(),
                classData.getSchoolYear(),
                classData.getSection(),
                classData.getSemester(),
                classData.getCreatedBy().getFirstname(),
                classData.getCreatedBy().getLastname(),
                classData.getCreatedBy().getRole(),
                classData.getMaxTeamSize()
        );
    }

    // Get a class by course code
    public ClassesDTO getClassByCourseCode(String courseCode, String section) {
        ClassesDTO response = new ClassesDTO();
        try {
            Optional<Classes> classByCourseCodeAndSection = cRepo.findByCourseCodeAndSectionPage(courseCode, section);
            if (classByCourseCodeAndSection.isPresent()) {
                response.setClasses(classByCourseCodeAndSection.get());
                response.setStatusCode(200);
                response.setMessage("Class with course code and section found successfully");
            } else {
                response.setStatusCode(404);
                response.setMessage("No class found with course code and section");
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while fetching class by course code and section: " + e.getMessage());
        }
        return response;
    }

    public ClassesDTO getClassByCourseCodeStudent(String courseCode, String section) {
        ClassesDTO response = new ClassesDTO();
        try {
            Optional<Classes> classByCourseCodeAndSection = cRepo.findByCourseCodeAndSectionPage(courseCode, section);
            if (classByCourseCodeAndSection.isPresent()) {
                response.setClasses(classByCourseCodeAndSection.get());
                response.setStatusCode(200);
                response.setMessage("Class with course code and section found successfully");
            } else {
                response.setStatusCode(404);
                response.setMessage("No class found with course code and section");
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while fetching class by course code and section: " + e.getMessage());
        }
        return response;
    }


    public ClassesDTO updateClass(Long classId, ClassesDTO classRequest) {
        ClassesDTO response;
        try {
            Optional<Classes> classOptional = cRepo.findById(classId);
            if (classOptional.isEmpty()) {
                return new ClassesDTO(404, "Class with ID '" + classId + "' not found", (List<Classes>) null);
            }

            Classes existingClass = classOptional.get();
            existingClass.setCourseCode(classRequest.getCourseCode());
            existingClass.setSection(classRequest.getSection());
            existingClass.setSchoolYear(classRequest.getSchoolYear());
            existingClass.setSemester(classRequest.getSemester());
            existingClass.setCourseDescription(classRequest.getCourseDescription());
            existingClass.setMaxTeamSize(classRequest.getMaxTeamSize());

            Classes updatedClass = cRepo.save(existingClass);

            response = new ClassesDTO(
                    updatedClass.getCid(),
                    updatedClass.getClassKey(),
                    updatedClass.getCourseCode(),
                    updatedClass.getCourseDescription(),
                    updatedClass.getSchoolYear(),
                    updatedClass.getSection(),
                    updatedClass.getSemester(),
                    updatedClass.getCreatedBy().getFirstname(),
                    updatedClass.getCreatedBy().getLastname(),
                    updatedClass.getCreatedBy().getRole(),
                    updatedClass.getMaxTeamSize()
            );

            response.setStatusCode(200);
            response.setMessage("Class updated successfully");

        } catch (Exception e) {
            response = new ClassesDTO(500, "Error occurred while updating class", e.getMessage());
        }
        return response;
    }


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

    public ClassesDTO getClassKeyByCourseCodeAndSection(String courseCode, String section) {
        ClassesDTO response;
        try {
            Classes classByCourseCodeAndSection = cRepo.findByCourseCodeAndSection(courseCode, section);
            if (classByCourseCodeAndSection != null) {
                response = new ClassesDTO();
                response.setClassKey(classByCourseCodeAndSection.getClassKey());
                response.setStatusCode(200);
                response.setMessage("ClassKey for course code and section retrieved successfully");
            } else {
                response = new ClassesDTO(404, "No class found with course code and section", (List<Classes>) null);
            }
        } catch (Exception e) {
            response = new ClassesDTO(500, "Error occurred while fetching classKey", e.getMessage());
        }
        return response;
    }


    public ClassesDTO enrollStudentByClassKey(String classKey, String email) {
        ClassesDTO response = new ClassesDTO();

        try {
            Optional<Classes> optionalClass = cRepo.findByClassKey(classKey);
            if (optionalClass.isEmpty()) {
                response.setStatusCode(404);
                response.setMessage("Class not found or is deleted");
                return response;
            }

            Classes clazz = optionalClass.get();

            Optional<User> optionalUser = uRepo.findByEmail(email);
            if (optionalUser.isEmpty()) {
                response.setStatusCode(404);
                response.setMessage("User not found");
                return response;
            }

            User student = optionalUser.get();
            //kung na enroll na
            if (clazz.getEnrolledStudents().contains(student)) {
                response.setStatusCode(400);
                response.setMessage("Student is already enrolled in the class");
                return response;
            }
            //kung wala
            clazz.getEnrolledStudents().add(student);
            cRepo.save(clazz);
            response.setStatusCode(200);
            response.setMessage("Student enrolled successfully");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while enrolling student: " + e.getMessage());
        }

        return response;
    }


    public Object[] getTotalUsersInClass(Long classId) {
        return cRepo.findTotalUsersInClass(classId);
    }

    public List<ClassesDTO> getClassesCreatedByUser(int userId) {
        try {
            List<ClassesDTO> classes = cRepo.findClassesByCreator2(userId);
            if (classes == null || classes.isEmpty()) {
                throw new NoSuchElementException("No classes found for the given user.");
            }
            return classes;
        } catch (NoSuchElementException e) {
            throw new RuntimeException("No classes found: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching classes created by user: " + e.getMessage());
        }
    }

    public List<UserDTO> getStudentsInClass(String classKey) {
        try {
            return cRepo.findStudentsInClass(classKey);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching students in class: " + e.getMessage());
        }
    }

    public List<ClassesDTO> getClassesForStudent(int studentId) {
        try {
            return cRepo.findClassesEnrolledByStudent(studentId);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching classes for student: " + e.getMessage());
        }
    }
    @Transactional
    public ClassesDTO removeStudentFromClass(String classKey, String email) {
        ClassesDTO response = new ClassesDTO();

        try {
            Optional<Classes> optionalClass = cRepo.findByClassKey(classKey);
            if (optionalClass.isEmpty()) {
                response.setStatusCode(404);
                response.setMessage("Class not found or is deleted");
                return response;
            }

            Classes clazz = optionalClass.get();

            // Find the user by their email
            Optional<User> optionalUser = uRepo.findByEmail(email);
            if (optionalUser.isEmpty()) {
                response.setStatusCode(404);
                response.setMessage("User not found");
                return response;
            }

            User student = optionalUser.get();

            if (!clazz.getEnrolledStudents().contains(student)) {
                response.setStatusCode(400);
                response.setMessage("Student is not enrolled in this class");
                return response;
            }
            // Remove the student from the class
            clazz.getEnrolledStudents().remove(student);
            cRepo.save(clazz);

            List<Team> teamsWithStudent = tRepo.findActiveTeamsByMemberId(student.getUid());
            for (Team team : teamsWithStudent) {
                if (team.getLeader().getUid() == student.getUid()) {
                    team.setDeleted(true);
                    tRepo.save(team);

                    if (team.getProject() != null) {
                        ppRepo.findById(team.getProject().getPid()).ifPresent(project -> {
                            project.setDeleted(true);
                            ppRepo.save(project);
                        });
                    }
                } else {
                    team.getMembers().remove(student);
                    tRepo.save(team);
                }
            }

            List<Team> teamsCreatedByLeader = tRepo.findActiveTeamsByLeaderId(student.getUid());
            for (Team team : teamsCreatedByLeader) {
                team.setDeleted(true);
                tRepo.save(team);

                if (team.getProject() != null) {
                    ppRepo.findById(team.getProject().getPid()).ifPresent(project -> {
                        project.setDeleted(true);
                        ppRepo.save(project);
                    });
                }
            }
            List<ProjectProposal> proposalsByStudent = ppRepo.findAllByProposedBy(student.getUid());
            for (ProjectProposal proposal : proposalsByStudent) {
                if (!proposal.getIsDeleted()) {
                    proposal.setDeleted(true);
                    ppRepo.save(proposal);
                }
            }
            response.setStatusCode(200);
            response.setMessage("Student removed successfully from the class, associated teams and project proposals updated.");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while removing student: " + e.getMessage());
        }

        return response;
    }


}

