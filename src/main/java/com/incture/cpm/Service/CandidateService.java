package com.incture.cpm.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.incture.cpm.Entity.Candidate;
import com.incture.cpm.helper.Helper;
import com.incture.cpm.Repo.CandidateRepository;

import java.io.IOException;
import java.util.List;

@Service

public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;


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
}
