package com.group2.SPEAR_Backend.Service;

import com.group2.SPEAR_Backend.DTO.EvaluationDTO;
import com.group2.SPEAR_Backend.Model.Classes;
import com.group2.SPEAR_Backend.Model.Evaluation;
import com.group2.SPEAR_Backend.Model.EvaluationType;
import com.group2.SPEAR_Backend.Model.Team;
import com.group2.SPEAR_Backend.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class EvaluationService {

    @Autowired
    private EvaluationRepository eRepo;

    @Autowired
    private ClassesRepository cRepo;

    @Autowired
    private UserRepository uRepo;

    @Autowired
    private QuestionRepository qRepo;

    @Autowired
    private TeamRepository tRepo;

    public EvaluationDTO createEvaluation(Evaluation evaluation, Long classId, EvaluationType evaluationType) {
        Classes classes = cRepo.findById(classId)
                .orElseThrow(() -> new NoSuchElementException("Class not found with ID: " + classId));

        validateDates(evaluation.getDateOpen(), evaluation.getDateClose());

        evaluation.setClassRef(classes);
        evaluation.setEvaluationType(evaluationType);
        evaluation.setAvailability(calculateAvailability(evaluation.getDateOpen(), evaluation.getDateClose()));

        Evaluation savedEvaluation = eRepo.save(evaluation);

        return new EvaluationDTO(
                savedEvaluation.getEid(),
                savedEvaluation.getEvaluationType(),
                savedEvaluation.getAvailability(),
                savedEvaluation.getDateOpen(),
                savedEvaluation.getDateClose(),
                savedEvaluation.getPeriod(),
                savedEvaluation.getClassRef().getCid(),
                savedEvaluation.getClassRef().getCourseCode(),
                savedEvaluation.getClassRef().getSection(),
                savedEvaluation.getClassRef().getCourseDescription(),
                null, // Team Name will be fetched dynamically
                null, // Adviser Name will be fetched dynamically
                null, // Evaluators fetched dynamically
                null, // Evaluatees fetched dynamically
                false // Evaluated status fetched dynamically
        );
    }
    public EvaluationDTO updateEvaluation(Long id, Evaluation updatedEvaluation, EvaluationType evaluationType) {
        validateDates(updatedEvaluation.getDateOpen(), updatedEvaluation.getDateClose());

        return eRepo.findById(id).map(evaluation -> {
            evaluation.setDateOpen(updatedEvaluation.getDateOpen());
            evaluation.setDateClose(updatedEvaluation.getDateClose());
            evaluation.setPeriod(updatedEvaluation.getPeriod());
            evaluation.setEvaluationType(evaluationType);
            evaluation.setAvailability(calculateAvailability(updatedEvaluation.getDateOpen(), updatedEvaluation.getDateClose()));

            Evaluation savedEvaluation = eRepo.save(evaluation);

            return new EvaluationDTO(
                    savedEvaluation.getEid(),
                    savedEvaluation.getEvaluationType(),
                    savedEvaluation.getAvailability(),
                    savedEvaluation.getDateOpen(),
                    savedEvaluation.getDateClose(),
                    savedEvaluation.getPeriod(),
                    savedEvaluation.getClassRef().getCid(),
                    savedEvaluation.getClassRef().getCourseCode(),
                    savedEvaluation.getClassRef().getSection(),
                    savedEvaluation.getClassRef().getCourseDescription(),
                    null, // Team Name - Will be retrieved dynamically
                    null, // Adviser Name - Will be retrieved dynamically
                    null, // Evaluator Names - Will be retrieved dynamically
                    null, // Evaluatee Names - Will be retrieved dynamically
                    false // Evaluated Status - Will be retrieved dynamically
            );
        }).orElseThrow(() -> new NoSuchElementException("Evaluation not found with ID: " + id));
    }


    public String deleteEvaluation(Long id) {
        if (eRepo.existsById(id)) {
            eRepo.deleteById(id);
            return "Evaluation deleted successfully";
        } else {
            throw new NoSuchElementException("Evaluation not found with ID: " + id);
        }
    }

    //all get functions
    public List<EvaluationDTO> getEvaluationsByClassAsDTO(Long classId) {
        return eRepo.findByClassRef_Cid(classId).stream()
                .map(evaluation -> new EvaluationDTO(
                        evaluation.getEid(),
                        evaluation.getEvaluationType(),
                        evaluation.getAvailability(),
                        evaluation.getDateOpen(),
                        evaluation.getDateClose(),
                        evaluation.getPeriod(),
                        evaluation.getClassRef().getCid(),
                        evaluation.getClassRef().getCourseCode(),
                        evaluation.getClassRef().getSection(),
                        evaluation.getClassRef().getCourseDescription(),
                        null, // Team Name will be fetched dynamically
                        null, // Adviser Name will be fetched dynamically
                        null, // Evaluators fetched dynamically
                        null, // Evaluatees fetched dynamically
                        false // Evaluated status fetched dynamically
                ))
                .collect(Collectors.toList());
    }

    public List<Evaluation> getEvaluationsByType(EvaluationType evaluationType) {
        return eRepo.findByEvaluationType(evaluationType);
    }

    public List<Evaluation> getEvaluationsByTypeAndPeriod(EvaluationType evaluationType, String period) {
        return eRepo.findByTypeAndPeriod(evaluationType, period);
    }

    public List<Evaluation> getEvaluationsByPeriod(String period) {
        return eRepo.findByPeriod(period);
    }

    public List<Evaluation> getEvaluationsByAvailabilityAndPeriod(String availability, String period) {
        return eRepo.findByAvailabilityAndPeriod(availability, period);
    }

    public List<Evaluation> getEvaluationsByAvailability(String availability) {
        return eRepo.findByAvailability(availability);
    }

    public String getYourTeamMembers(Long classId, Long studentId) {
        List<String> members = eRepo.findYourTeamMembers(classId, studentId);
        return String.join(", ", members); // Convert list to string
    }

    public Long getTeamId(Long studentId, Long classId) {
        return eRepo.findTeamIdByStudent(studentId, classId);
    }


    public List<EvaluationDTO> getEvaluationsForStudent(Long studentId) {
        return eRepo.findOpenEvaluationsForStudent(studentId).stream()
                .map(evaluation -> new EvaluationDTO(
                        evaluation.getEid(),
                        evaluation.getEvaluationType(),
                        evaluation.getAvailability(),
                        evaluation.getDateOpen(),
                        evaluation.getDateClose(),
                        evaluation.getPeriod(),
                        evaluation.getClassRef().getCid(),
                        evaluation.getClassRef().getCourseCode(),
                        evaluation.getClassRef().getSection(),
                        evaluation.getClassRef().getCourseDescription(),
                        null, // Team Name
                        null, // Adviser Name
                        null, // Evaluators
                        null, // Evaluatees
                        false  // Evaluated status
                ))
                .collect(Collectors.toList());
    }



    public List<EvaluationDTO> getEvaluationsForAdviser(Long adviserId) {
        return eRepo.findOpenEvaluationsForAdviser(adviserId).stream()
                .flatMap(evaluation -> {
                    // Fetch all teams for the adviser within the evaluation's class
                    List<Team> teams = tRepo.findByClassIdAndAdviserId(evaluation.getClassRef().getCid(), adviserId);

                    // Map each team separately
                    return teams.stream().map(team -> new EvaluationDTO(
                            evaluation.getEid(),
                            evaluation.getEvaluationType(),
                            evaluation.getAvailability(),
                            evaluation.getDateOpen(),
                            evaluation.getDateClose(),
                            evaluation.getPeriod(),
                            evaluation.getClassRef().getCid(),
                            evaluation.getClassRef().getCourseCode(),
                            evaluation.getClassRef().getSection(),
                            evaluation.getClassRef().getCourseDescription(),
                            team.getGroupName(), // Fetch correct team name
                            team.getAdviser() != null
                                    ? team.getAdviser().getFirstname() + " " + team.getAdviser().getLastname()
                                    : "Unknown Adviser",
                            null, // Evaluators
                            null, // Evaluatees
                            false  // Evaluated status
                    ));
                })
                .collect(Collectors.toList());
    }
    //for admin download
    public List<EvaluationDTO> getAllAdviserToStudentEvaluations() {
        return eRepo.findAllAdviserToStudentEvaluations().stream()
                .map(evaluation -> new EvaluationDTO(
                        evaluation.getEid(),
                        evaluation.getEvaluationType(),
                        evaluation.getAvailability(),
                        evaluation.getDateOpen(),
                        evaluation.getDateClose(),
                        evaluation.getPeriod(),
                        evaluation.getClassRef().getCid(),
                        evaluation.getClassRef().getCourseCode(),
                        evaluation.getClassRef().getSection(),
                        evaluation.getClassRef().getCourseDescription(),
                        null,  // Team Name
                        null,  // Adviser Name
                        null,  // Evaluators
                        null,  // Evaluatees
                        false  // Evaluated status
                ))
                .collect(Collectors.toList());
    }




    //for dates, do not touch!
    // Helper method to calculate availability based on open and close dates
    private String calculateAvailability(LocalDate dateOpen, LocalDate dateClose) {
        LocalDate today = LocalDate.now();
        if (today.isBefore(dateOpen)) {
            return "Closed"; // Before dateOpen
        } else if ((today.isEqual(dateOpen) || today.isAfter(dateOpen)) && today.isBefore(dateClose)) {
            return "Open"; // On or after dateOpen but before dateClose
        } else {
            return "Closed"; // On or after dateClose
        }
    }

    // Scheduler to update evaluations every 15 seconds
    @Scheduled(fixedRate = 15000) // Executes every 15 seconds
    public void refreshEvaluationsAvailability() {
        List<Evaluation> allEvaluations = eRepo.findAll();
        for (Evaluation evaluation : allEvaluations) {
            String calculatedAvailability = calculateAvailability(evaluation.getDateOpen(), evaluation.getDateClose());
            if (!calculatedAvailability.equals(evaluation.getAvailability())) {
                evaluation.setAvailability(calculatedAvailability);
                eRepo.save(evaluation);
            }
        }
    }

    private void validateDates(LocalDate dateOpen, LocalDate dateClose) {
        if (dateOpen == null || dateClose == null) {
            throw new IllegalArgumentException("Date open and date close cannot be null.");
        }

        // Ensure dateOpen is strictly earlier than dateClose
        if (!dateOpen.isBefore(dateClose)) {
            throw new IllegalArgumentException("Date open must be earlier than date close.");
        }
    }

}
