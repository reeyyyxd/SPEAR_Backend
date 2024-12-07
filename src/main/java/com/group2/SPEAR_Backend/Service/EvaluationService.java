package com.group2.SPEAR_Backend.Service;

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

    public Evaluation createEvaluation(Evaluation evaluation, Long classId, Long evaluatorId) {
        // Fetch the class
        cRepo.findById(classId)
                .orElseThrow(() -> new NoSuchElementException("Class not found with ID: " + classId));

        // Remove the question count check
        evaluation.setAvailability(calculateAvailability(evaluation.getDateOpen(), evaluation.getDateClose()));

        return eRepo.save(evaluation);
    }

    public List<Evaluation> getAllEvaluations() {
        return eRepo.findAll();
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

    public Evaluation updateEvaluation(Long id, Evaluation updatedEvaluation) {
        return eRepo.findById(id).map(evaluation -> {
            evaluation.setStatus(updatedEvaluation.getStatus());
            evaluation.setDateOpen(updatedEvaluation.getDateOpen());
            evaluation.setDateClose(updatedEvaluation.getDateClose());
            evaluation.setPeriod(updatedEvaluation.getPeriod());
            evaluation.setAvailability(calculateAvailability(updatedEvaluation.getDateOpen(), updatedEvaluation.getDateClose()));

            return eRepo.save(evaluation);
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
        if (today.isAfter(dateOpen) && today.isBefore(dateClose)) {
            return "Open";
        } else if (today.isAfter(dateClose)) {
            return "Closed";
        }
        return "Pending";
    }

    // Scheduler to update evaluations with "Late" status if date_close has passed and status is not "Completed"
    @Scheduled(cron = "0 0 0 * * *") // Runs daily at midnight
    public void updateLateEvaluations() {
        List<Evaluation> allEvaluations = eRepo.findAll();
        LocalDate today = LocalDate.now();

        for (Evaluation evaluation : allEvaluations) {
            if (evaluation.getDateClose().isBefore(today) && !"Completed".equalsIgnoreCase(evaluation.getStatus())) {
                evaluation.setStatus("Late");
                eRepo.save(evaluation);
            }
        }
    }
}
