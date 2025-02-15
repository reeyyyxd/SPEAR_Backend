package com.group2.SPEAR_Backend.Controller;

import com.group2.SPEAR_Backend.DTO.InterestDTO;
import com.group2.SPEAR_Backend.Service.InterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class InterestController {

    @Autowired
    private InterestService iServ;

    @PostMapping("/create-interest")
    public ResponseEntity<InterestDTO> createInterest(@RequestBody InterestDTO interestDTO) {
        InterestDTO createdInterest = iServ.createInterest(interestDTO.getUserInterest().getUid(), interestDTO.getInterest(), interestDTO.getDepartment());
        return ResponseEntity.ok(createdInterest);
    }


    @GetMapping("view-interest/user/{userId}")
    public ResponseEntity<InterestDTO> getInterestByUser(@PathVariable int userId) {
        InterestDTO interestDTO = iServ.getInterestByUser(userId);
        return ResponseEntity.ok(interestDTO);
    }

    @PutMapping("/update-interest/{iid}")
    public ResponseEntity<InterestDTO> updateInterest(@PathVariable int iid, @RequestBody InterestDTO interestDTO) {
        InterestDTO updatedInterest = iServ.updateInterest(iid, interestDTO.getInterest());
        return ResponseEntity.ok(updatedInterest);
    }



    @DeleteMapping("/delete-interest/{iid}")
    public ResponseEntity<String> deleteInterest(@PathVariable int iid) {
        iServ.deleteInterest(iid);
        return ResponseEntity.ok("Interest deleted successfully");
    }
}
