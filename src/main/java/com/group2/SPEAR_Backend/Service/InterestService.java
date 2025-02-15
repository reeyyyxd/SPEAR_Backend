package com.group2.SPEAR_Backend.Service;

import com.group2.SPEAR_Backend.DTO.InterestDTO;
import com.group2.SPEAR_Backend.Model.Interest;
import com.group2.SPEAR_Backend.Model.User;
import com.group2.SPEAR_Backend.Repository.InterestRepository;
import com.group2.SPEAR_Backend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InterestService {

    @Autowired
    private InterestRepository iRepo;

    @Autowired
    private UserRepository uRepo;

    public InterestDTO createInterest(int userId, String interest, String department) {
        User user = uRepo.findById(Math.toIntExact(userId)).orElseThrow(() -> new RuntimeException("User not found"));
        Interest newInterest = new Interest(user, interest, department);
        iRepo.save(newInterest);
        return new InterestDTO(newInterest.getIid(), newInterest.getUserInterest(), newInterest.getInterest(), newInterest.getDepartment());
    }

    public InterestDTO getInterestByUser(int userId) {
        Interest interest = iRepo.findByUserInterest_Uid(userId)
                .orElseThrow(() -> new RuntimeException("No interest found for user"));
        return new InterestDTO(interest.getIid(), interest.getUserInterest(), interest.getInterest(), interest.getDepartment());
    }

    public InterestDTO updateInterest(int iid, String newInterest) {
        Interest interest = iRepo.findById(iid)
                .orElseThrow(() -> new RuntimeException("Interest not found"));

        interest.setInterest(newInterest);
        iRepo.save(interest);

        return new InterestDTO(interest.getIid(), interest.getUserInterest(), interest.getInterest(), interest.getDepartment());
    }

    public void deleteInterest(int iid) {
        Interest interest = iRepo.findById(iid)
                .orElseThrow(() -> new RuntimeException("Interest not found"));

        interest.setUserInterest(null);
        iRepo.delete(interest);
    }

}
