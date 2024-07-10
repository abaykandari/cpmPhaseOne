package com.incture.cpm.Service;

import com.incture.cpm.Entity.CollegeTPO;
import com.incture.cpm.Repo.CollegeTPORepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class CollegeTpoService {
    @Autowired
    private CollegeTPORepo myTPORepo;

    public CollegeTPO insertFunction(CollegeTPO collegeTPO) {
        // collegeTPO.setCollegeId(generateRandomInt());
        return this.myTPORepo.save(collegeTPO);
    }


    public List<CollegeTPO> findData() {
        return this.myTPORepo.findAll();
    }

    public CollegeTPO getCollegeTPOById(int collegeId) {
        return myTPORepo.findById(collegeId).orElse(null);
    }

    public CollegeTPO updateCollegeTPO(int collegeId, CollegeTPO collegeTPO) {
        Optional<CollegeTPO> existingCollegeTPOOptional = myTPORepo.findById(collegeId);
        if (existingCollegeTPOOptional.isPresent()) {
            CollegeTPO existingCollegeTPO = existingCollegeTPOOptional.get();
            existingCollegeTPO.setCollegeName(collegeTPO.getCollegeName());
            existingCollegeTPO.setTpoName(collegeTPO.getTpoName());
            existingCollegeTPO.setPrimaryEmail(collegeTPO.getPrimaryEmail());
            existingCollegeTPO.setPhoneNumber(collegeTPO.getPhoneNumber());
            existingCollegeTPO.setAddressLine1(collegeTPO.getAddressLine1());
            existingCollegeTPO.setAddressLine2(collegeTPO.getAddressLine2());
            existingCollegeTPO.setLocation(collegeTPO.getLocation());
            existingCollegeTPO.setRegion(collegeTPO.getRegion());
            existingCollegeTPO.setCollegeOwner(collegeTPO.getCollegeOwner());
            existingCollegeTPO.setPrimaryContact(collegeTPO.getPrimaryContact());
            existingCollegeTPO.setSecondaryContact(collegeTPO.getSecondaryContact());
            existingCollegeTPO.setTier(collegeTPO.getTier());
            existingCollegeTPO.setPinCode(collegeTPO.getPinCode());
            existingCollegeTPO.setState(collegeTPO.getState());
            existingCollegeTPO.setCompensation(collegeTPO.getCompensation());
            return myTPORepo.save(existingCollegeTPO);
        } else {
            return null; // Or throw an exception indicating that the collegeId was not found
        }

    }

    public void deleteCollegeTPO(int collegeId) {
        myTPORepo.deleteById(collegeId);

    }
/* 
    private int generateRandomInt() {
        Random random = new Random();
        return random.nextInt(Integer.MAX_VALUE);
    }
 */
}
