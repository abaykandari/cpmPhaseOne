package com.incture.cpm.Service;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.incture.cpm.Entity.Candidate;
import com.incture.cpm.Entity.CollegeTPO;
import com.incture.cpm.helper.Helper;
 
import com.incture.cpm.Repo.CandidateRepository;
import com.incture.cpm.Repo.CollegeRepository;
import com.incture.cpm.Util.ExcelUtil;
 
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
 
@Service
public class CandidateService {
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private ExcelUtil excelUtil;
    @Autowired
    private CollegeRepository collegeRepository;
 
    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }
 
    public Candidate getCandidateById(Long id) {
        return candidateRepository.findById(id).orElse(null);
    }
 
    public Candidate createCandidate(Candidate candidate) {
        return candidateRepository.save(candidate);
    }
 
    public Candidate updateCandidate(Long id, Candidate candidate) {
        candidate.setCandidateId(id);
        return candidateRepository.save(candidate);
    }
 
    public void deleteCandidate(Long id) {
        candidateRepository.deleteById(id);
    }
 
    // ----------------------------------------------------------------
   
   
    public void save(MultipartFile file) {
 
        try {
            List<Candidate> candidates = Helper.convertExcelToListOfProduct(file.getInputStream());
            this.candidateRepository.saveAll(candidates);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    public List<Candidate> getAllProducts() {
        return this.candidateRepository.findAll();
    }
 
    @Transactional
    public void feedCandidateData(MultipartFile file, int collegeId) throws NotFoundException {
        try {
            List<Map<String, String>> dataList = excelUtil.readExcelFile(file);
   
            for (Map<String, String> data : dataList) {
                Optional<CollegeTPO> clg = collegeRepository.findById(collegeId);
                if(clg.isEmpty()) throw new NotFoundException();
               
                Candidate cand = new Candidate();
                cand.setCandidateName(data.get("Name"));
                cand.setEmail(data.get("email"));
                cand.setTenthPercent(Double.parseDouble(data.get("10th %")));
                cand.setTwelthPercent(Double.parseDouble(data.get("12th %")));
                cand.setFatherName(data.get("fatherName"));
                cand.setPermanentAddress(data.get("address"));
                cand.setMotherName(data.get("motherName"));
                cand.setCgpaUndergrad(Double.parseDouble(data.get("CGPA U")));
                cand.setCgpaMasters(Double.parseDouble(data.get("CGPA M")));
                cand.setPhoneNumber(data.get("phoneNum"));
               
                cand.setCurrentLocation(data.get("location"));
                cand.setAadhaarNumber(data.get("adhaar"));
                cand.setDOB(data.get("DOB"));
                cand.setDepartment(data.get("department"));
                cand.setPanNumber(data.get("pan"));
                cand.setAlternateNumber(data.get("alterNate"));
                cand.setCollegeId(collegeId);
               
                cand.setStatus("interview pending");
   
                cand.setCandidateCollege(clg.get().getCollegeName());
                candidateRepository.save(cand);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
        }
    }
   
}