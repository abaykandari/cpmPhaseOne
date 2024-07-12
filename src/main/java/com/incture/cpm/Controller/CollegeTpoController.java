package com.incture.cpm.Controller;

import com.incture.cpm.Entity.ArchievedColleges;
import com.incture.cpm.Entity.CollegeTPO;
import com.incture.cpm.Repo.ArchievedCollegeRepo;
import com.incture.cpm.Repo.CollegeTPORepo;
import com.incture.cpm.Service.ArchieveService;
import com.incture.cpm.Service.CollegeTpoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class CollegeTpoController {
    @Autowired
    private CollegeTpoService myTpoService;
    @Autowired
    private ArchievedCollegeRepo archievedCollegeRepo;
    @Autowired
    private CollegeTPORepo collegeTPORepo;
    @Autowired
    private ArchieveService archieveService;
    //Create a collegeTpo
    @PostMapping("/insertCollegeData")   //frontend me add CollegeData
    public ResponseEntity<CollegeTPO> insertCollData( @RequestBody CollegeTPO collegeTPO){

        return ResponseEntity.ok(myTpoService.insertFunction(collegeTPO));
   }


//    @CrossOrigin(origins = "http://localhost:5173")
   @GetMapping("/viewData")
    public List<CollegeTPO> viewData(){
        return myTpoService.findData();
   }
    @GetMapping("/viewData/{collegeId}")
    public CollegeTPO getCollegeTPOById(@PathVariable int collegeId) {
        return myTpoService.getCollegeTPOById(collegeId);

    }
    //Update CollegeTpo
    @PutMapping("/updateData/{collegeId}")
    public ResponseEntity<CollegeTPO> updateCollegeTPO(@PathVariable int collegeId, @RequestBody CollegeTPO collegeTPO) {
        CollegeTPO updatedCollegeTPO = myTpoService.updateCollegeTPO(collegeId, collegeTPO);
        if (updatedCollegeTPO != null) {
            return ResponseEntity.ok(updatedCollegeTPO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    //update by all fields
    //delete the CollegeTpo
    @DeleteMapping("/deleteData/{collegeId}")
    public void deleteCollegeTPO(@PathVariable int collegeId) {
        myTpoService.deleteCollegeTPO(collegeId);
    }

@DeleteMapping("/deleteandarchieve")
public String deleteEntities(@RequestBody List<Integer> ids) throws Exception{
    for (Integer id : ids) {
        CollegeTPO existingCollegeTpo=collegeTPORepo.findById(id).orElse(null);
        if(existingCollegeTpo!=null) {
            ArchievedColleges archievedColleges=converterMethod(existingCollegeTpo);
            archievedCollegeRepo.save(archievedColleges);
            myTpoService.deleteCollegeTPO(id);

        }
    }
    return "Entities deleted and archieved successfully";
}
public ArchievedColleges converterMethod(CollegeTPO collegeTPO){
        ArchievedColleges archievedColleges=new ArchievedColleges();
        archievedColleges.setCollegeName(collegeTPO.getCollegeName());
        archievedColleges.setCollegeOwner(collegeTPO.getCollegeOwner());
        archievedColleges.setCompensation(collegeTPO.getCompensation());
        archievedColleges.setAddressLine1(collegeTPO.getAddressLine1());
        archievedColleges.setAddressLine2(collegeTPO.getAddressLine2());
        archievedColleges.setLocation(collegeTPO.getLocation());
        archievedColleges.setPhoneNumber(collegeTPO.getPhoneNumber());
        archievedColleges.setPinCode(collegeTPO.getPinCode());
        archievedColleges.setPrimaryContact(collegeTPO.getPrimaryContact());
        archievedColleges.setTier(collegeTPO.getTier());
        archievedColleges.setSecondaryContact(collegeTPO.getSecondaryContact());
        archievedColleges.setTpoName(collegeTPO.getTpoName());
        archievedColleges.setState(collegeTPO.getState());
        archievedColleges.setRegion(collegeTPO.getRegion());
        return archievedColleges;

}
@GetMapping("/readarchievecollege")
    public ResponseEntity<List<ArchievedColleges>> view(){
        return ResponseEntity.ok(archieveService.view());
}
}
