package com.incture.cpm.Service;

import com.incture.cpm.Entity.ArchievedColleges;
import com.incture.cpm.Repo.ArchievedCollegeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArchieveService {
    @Autowired
    private ArchievedCollegeRepo archievedCollegeRepo;
    public List<ArchievedColleges> view(){
        return archievedCollegeRepo.findAll();
    }
}
