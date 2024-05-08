package com.example.cpm.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.example.cpm.Entity.Talent;
import com.example.cpm.Repo.TalentRepository;

@Service
public class TalentService {

    @Autowired
    private TalentRepository talentRepository;

    public Talent createTalent(Talent talent) {
        return talentRepository.save(talent);
    }

    public List<Talent> getAllTalents() {
        return talentRepository.findAll();
    }

    public Talent getTalentById(int talentId) {
        return talentRepository.findById(talentId).orElse(null);
    }

    public Talent updateTalent(Talent talent,int talentId) {
        Talent existingTalent = talentRepository.findById(talentId).orElse(null);

        if (existingTalent != null) {
           talent.setTalentId(talentId);
            return  talentRepository.save(talent);
        }
        return null;
    }

    public void deleteTalent(int talentId) {
        talentRepository.deleteById(talentId);
    }
}