package com.incture.cpm.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.cpm.Entity.Candidate;
import com.incture.cpm.Entity.Talent;
import com.incture.cpm.Repo.TalentRepository;

@Service
public class TalentService {

    @Autowired
    private TalentRepository talentRepository;

    public Talent addTalentFromCandidate(Candidate candidate){
        Talent existingtTalent=talentRepository.findByCandidateId(candidate.getCandidateId());
        if(existingtTalent!=null){
            return null;
        }
        Talent newTalent= new Talent();
        newTalent.setCandidateId(candidate.getCandidateId());
        newTalent.setTalentName(candidate.getCandidateName());
        newTalent.setCollegeName(candidate.getCandidateCollege());
        newTalent.setDepartment(candidate.getDepartment());
        newTalent.setEmail(candidate.getEmail());
        newTalent.setPhoneNumber(candidate.getPhoneNumber());
        newTalent.setAlternateNumber(candidate.getAlternateNumber());
        newTalent.setTenthPercent(candidate.getTenthPercent());
        newTalent.setTwelthPercent(candidate.getTwelthPercent());
        newTalent.setCurrentLocation(candidate.getCurrentLocation());
        newTalent.setPermanentAddress(candidate.getPermanentAddress());
        newTalent.setPanNumber(candidate.getPanNumber());
        newTalent.setAadhaarNumber(candidate.getAadhaarNumber());
        newTalent.setFatherName(candidate.getFatherName());
        newTalent.setMotherName(candidate.getMotherName());
        newTalent.setDob(candidate.getDOB());
        newTalent.setCgpaUndergrad(candidate.getCgpaUndergrad());
        newTalent.setCgpaMasters(candidate.getCgpaMasters());
        return talentRepository.save(newTalent);
    }

    public Talent createTalent(Talent talent) {
        return talentRepository.save(talent);
    }

    public List<Talent> getAllTalents() {
        return talentRepository.findAll();
    }

    public Talent getTalentById(Long talentId) {
        return talentRepository.findById(talentId).orElse(null);
    }

    public Talent updateTalent(Talent talent,Long talentId) {
        Talent existingTalent = talentRepository.findById(talentId).orElse(null);

        if (existingTalent != null) {
           talent.setTalentId(talentId);
           talent.setCandidateId(existingTalent.getCandidateId());
            return  talentRepository.save(talent);
        }
        return null;
    }

    public boolean deleteTalent(Long talentId) {
        Talent existingTalent= talentRepository.findById(talentId).orElse(null);
        if(existingTalent!=null){
            talentRepository.deleteById(talentId);
            return true;
        }
        return false;
    }
}