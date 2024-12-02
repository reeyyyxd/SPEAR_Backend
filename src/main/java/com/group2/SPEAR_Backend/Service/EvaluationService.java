package com.group2.SPEAR_Backend.Service;

import com.group2.SPEAR_Backend.Model.Classes;
import com.group2.SPEAR_Backend.Model.Evaluation;
import com.group2.SPEAR_Backend.Repository.ClassesRepository;
import com.group2.SPEAR_Backend.Repository.EvaluationRepository;
import com.group2.SPEAR_Backend.Repository.QuestionRepository;
import com.group2.SPEAR_Backend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        Classes clazz = cRepo.findById(classId)
                .orElseThrow(() -> new NoSuchElementException("Class not found with ID: " + classId));

        // Count the questions
        long questionCount = qRepo.findByClassesCid(classId).size();
        if (questionCount < 10) {
            throw new IllegalStateException("Evaluation cannot be created. Class must have 10 questions. Current count: " + questionCount);
        }

        evaluation.setClasses(clazz);

        evaluation.setEvaluator(uRepo.findById(evaluatorId.intValue())
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + evaluatorId)));

        LocalDate today = LocalDate.now();
        if (today.isAfter(evaluation.getDateOpen()) && today.isBefore(evaluation.getDateClose())) {
            evaluation.setAvailability("Open");
        } else {
            evaluation.setAvailability("Closed");
        }

        return eRepo.save(evaluation);
    }



    //to change
    public List<Evaluation> getAllEvaluations() {
        return eRepo.findAll();
    }

//    public List<Evaluation> getEvaluationsByAvailability(String availability) {
//        return eRepo.findByAvailability(availability);
//    }

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

            LocalDate today = LocalDate.now();
            if (today.isAfter(evaluation.getDateOpen()) && today.isBefore(evaluation.getDateClose())) {
                evaluation.setAvailability("Open");
            } else {
                evaluation.setAvailability("Closed");
            }

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
}
