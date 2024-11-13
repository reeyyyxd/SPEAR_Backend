//package com.group2.SPEAR_Backend.Service;
//
//import com.group2.SPEAR_Backend.Model.Evaluation;
//import com.group2.SPEAR_Backend.Repository.EvaluationRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.NoSuchElementException;
//
//@Service
//public class EvaluationService {
//
//    @Autowired
//    EvaluationRepository evaluationRepository;
//
//    // Create new evaluation
//    public Evaluation createEvaluation(Evaluation evaluation) {
//        return evaluationRepository.save(evaluation);
//    }
//
//    // Get all evaluations
//    public List<Evaluation> getAllEvaluations() {
//        return evaluationRepository.findAll();
//    }
//
//    // Update evaluation
//    public Evaluation updateEvaluation(int id, Evaluation updatedEvaluation) {
//        try {
//            Evaluation existingEvaluation = evaluationRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Evaluation with id " + id + " not found"));
//
//            existingEvaluation.setStatusForGrading(updatedEvaluation.getStatusForGrading());
//            existingEvaluation.setStatusForCompletion(updatedEvaluation.getStatusForCompletion());
//            existingEvaluation.setUserToEvaluate(updatedEvaluation.getUserToEvaluate());
//            existingEvaluation.setEvaluator(updatedEvaluation.getEvaluator());
//
//            return evaluationRepository.save(existingEvaluation);
//        } catch (NoSuchElementException e) {
//            throw new NoSuchElementException("Evaluation with id " + id + " not found");
//        }
//    }
//
//    // Delete evaluation
//    public String deleteEvaluation(int id) {
//        if (evaluationRepository.existsById(id)) {
//            evaluationRepository.deleteById(id);
//            return "Evaluation with id " + id + " deleted";
//        } else {
//            return "Evaluation with id " + id + " not found";
//        }
//    }
//}
