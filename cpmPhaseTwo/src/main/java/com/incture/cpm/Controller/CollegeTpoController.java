package com.incture.cpm.Controller;

import com.incture.cpm.Entity.CollegeTPO;
import com.incture.cpm.Service.CollegeTpoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class CollegeTpoController {
    @Autowired
    private CollegeTpoService myTpoService;
    //Create a collegeTpo
    @PostMapping("/insertCollegeData")   //frontend me add CollegeData
    public ResponseEntity<CollegeTPO> insertCollData( @RequestBody CollegeTPO collegeTPO){

        return ResponseEntity.ok(myTpoService.insertFunction(collegeTPO));
   }
   //read the collegesTpo
    
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
//    @DeleteMapping("/multipleDelete")
//    public String deleteEntities(@RequestBody List<Integer> ids) {
//        collegeTPORepo.deleteEntitiesByIdIn(ids);
//        return "Entities deleted successfully";
//    }
@DeleteMapping("/multipleDelete")
public String deleteEntities(@RequestBody List<Integer> ids) {
    for (Integer id : ids) {
        myTpoService.deleteCollegeTPO(id);
    }
    return "Entities deleted successfully";
}

}
