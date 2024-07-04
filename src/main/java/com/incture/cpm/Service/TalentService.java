package com.incture.cpm.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.cpm.Dto.TalentSummaryDto;
import com.incture.cpm.Entity.Candidate;
import com.incture.cpm.Entity.Talent;
import com.incture.cpm.Exception.ResourceNotFoundException;
import com.incture.cpm.Repo.TalentRepository;

@Service
public class TalentService {

    @Autowired
    private TalentRepository talentRepository;

    public Talent addTalentFromCandidate(Candidate candidate) {
        Talent existingtTalent = talentRepository.findByCandidateId(candidate.getCandidateId());
        if (existingtTalent != null) {
            return null;
        }
        Talent newTalent = new Talent();
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

    public Talent updateTalent(Talent talent, Long talentId) {
        Talent existingTalent = talentRepository.findById(talentId).orElse(null);

        if (existingTalent != null) {
            talent.setTalentId(talentId);
            talent.setCandidateId(existingTalent.getCandidateId());
            return talentRepository.save(talent);
        }
        return null;
    }

    public boolean deleteTalent(Long talentId) {
        Talent existingTalent = talentRepository.findById(talentId).orElse(null);
        if (existingTalent != null) {
            talentRepository.deleteById(talentId);
            return true;
        }
        return false;
    }

    // new added functionality

    // This functionality is for resigned employees
    public Talent resignTalent(Long talentId, String status, String resignationReason, String date) {
        Talent talent = talentRepository.findById(talentId).orElse(null);
        if (talent == null) {
            throw new ResourceNotFoundException("No Talent exists with given Talent Id");
        }
        talent.setTalentStatus(status);
        // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd
        // HH:mm:ss");
        // String message = LocalDateTime.now().format(formatter);
        String message = date;
        message = message + " -> " + resignationReason;
        talent.setExitReason(message);
        return talentRepository.save(talent);
    }

    // FOR STATS OF TALENT TABLE

    // public TalentSummaryDto talentStats() {
    // int totalTalents = talentRepository.countTotalTalents();
    // int activeTalents = talentRepository.countActiveTalents();
    // int inactiveTalents = talentRepository.countInactiveTalents();
    // int declinedTalents = talentRepository.countDeclinedTalents();
    // int resignedTalents = talentRepository.countResignedTalents();
    // int revokedTalents = talentRepository.countRevokedTalents();

    // return new TalentSummaryDto(
    // totalTalents,
    // activeTalents,
    // inactiveTalents,
    // declinedTalents,
    // resignedTalents,
    // revokedTalents);
    // }

}