package com.group2.SPEAR_Backend.Service;

import com.group2.SPEAR_Backend.DTO.EvaluationDTO;
import com.group2.SPEAR_Backend.Model.Classes;
import com.group2.SPEAR_Backend.Model.Evaluation;
import com.group2.SPEAR_Backend.Repository.ClassesRepository;
import com.group2.SPEAR_Backend.Repository.EvaluationRepository;
import com.group2.SPEAR_Backend.Repository.QuestionRepository;
import com.group2.SPEAR_Backend.Repository.UserRepository;
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

    public EvaluationDTO createEvaluation(Evaluation evaluation, Long classId) {
        Classes classes = cRepo.findById(classId)
                .orElseThrow(() -> new NoSuchElementException("Class not found with ID: " + classId));

        validateDates(evaluation.getDateOpen(), evaluation.getDateClose());

        evaluation.setClasses(classes);
        evaluation.setAvailability(calculateAvailability(evaluation.getDateOpen(), evaluation.getDateClose()));

        Evaluation savedEvaluation = eRepo.save(evaluation);

        return new EvaluationDTO(
                savedEvaluation.getDateOpen(),
                savedEvaluation.getDateClose(),
                savedEvaluation.getPeriod()
        );
    }





    public List<EvaluationDTO> getEvaluationsByClassAsDTO(Long classId) {
        return eRepo.findByClassesCid(classId).stream()
                .map(evaluation -> new EvaluationDTO(
                        evaluation.getEid(),
                        evaluation.getAvailability(),
                        evaluation.getDateOpen(),
                        evaluation.getDateClose(),
                        evaluation.getPeriod(),
                        evaluation.getClasses().getCid(),
                        evaluation.getClasses().getCourseCode(),
                        evaluation.getClasses().getSection(),
                        evaluation.getClasses().getCourseDescription()
                ))
                .collect(Collectors.toList());
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

    public EvaluationDTO updateEvaluation(Long id, Evaluation updatedEvaluation) {
        validateDates(updatedEvaluation.getDateOpen(), updatedEvaluation.getDateClose());

        return eRepo.findById(id).map(evaluation -> {
            evaluation.setDateOpen(updatedEvaluation.getDateOpen());
            evaluation.setDateClose(updatedEvaluation.getDateClose());
            evaluation.setPeriod(updatedEvaluation.getPeriod());
            evaluation.setAvailability(calculateAvailability(updatedEvaluation.getDateOpen(), updatedEvaluation.getDateClose()));

            Evaluation savedEvaluation = eRepo.save(evaluation);

            return new EvaluationDTO(
                    savedEvaluation.getDateOpen(),
                    savedEvaluation.getDateClose(),
                    savedEvaluation.getPeriod(),
                    savedEvaluation.getAvailability()
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


    public EvaluationDTO getEvaluationDetailsById(Long evaluationId) {
        return eRepo.findEvaluationDetailsById(evaluationId);
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
