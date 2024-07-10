package com.incture.cpm.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.incture.cpm.Entity.Mentor;
import com.incture.cpm.Entity.Talent;
import com.incture.cpm.Entity.Team;
import com.incture.cpm.Exception.ResourceNotFoundException;
import com.incture.cpm.Repo.MentorRepository;
import com.incture.cpm.Repo.TalentRepository;
import com.incture.cpm.Repo.TeamRepository;

@Service
public class MentorService {

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private TalentRepository talentRepository;

    @Autowired
    private TeamRepository teamRepository;


    public Optional<Mentor> getMentorById(Long mentorId) {
        return mentorRepository.findById(mentorId);
    }

    public List<Mentor> getAllMentors() {
        return mentorRepository.findAll();
    }

    public List<Mentor> getMentorsOfTeam(Long teamId) {
        List<Mentor> mentorList = mentorRepository.findByTeam(teamRepository.findById(teamId).get());
        if (mentorList.isEmpty()) {
            throw new ResourceNotFoundException("No Mentors Present in the team with Id " + teamId);
        }
        return mentorList;
    }

   
    public String addMentor(Long talentId, Long teamId) {
        Optional<Talent> talent = talentRepository.findById(talentId);
        if (talent.isEmpty()) {
            throw new ResourceNotFoundException("No Talent exist with given Talent Id");
        }
        Optional<Team> team = teamRepository.findById(teamId);
        if (team.isEmpty()) {
            throw new ResourceNotFoundException("Incorrect Team Id given");
        }
        // Create the Mentor
        Mentor createdMentor = new Mentor();
        createdMentor.setTalentId(talentId);
        createdMentor.setName(talent.get().getTalentName());
        createdMentor.setSkills(talent.get().getTalentSkills());

        // Add the Team to the Mentor
        Set<Team> teams = new HashSet<>();
        teams.add(team.get());
        createdMentor.setTeam(teams);

        // Save the Mentor to get its ID
        mentorRepository.save(createdMentor);

        // Add the Mentor to the Team
        Set<Mentor> mentors = team.get().getMentor();
        if (mentors == null) {
            mentors = new HashSet<>();
        }
        mentors.add(createdMentor);
        team.get().setMentor(mentors);

        // Save the Team to update the relationship
        teamRepository.save(team.get());

        return "Team Mentor Added Successfully";
    }


    
    public String deleteMentor(Long mentorId) {
        Optional<Mentor> mentor = mentorRepository.findById(mentorId);
        if (mentor.isEmpty()) {
            return "No Team mentor is present with given MentorId";
        }
        Set<Team> teams = mentor.get().getTeam();
        List<Long> ids=new ArrayList<>();
        for(Team team:teams){
            ids.add(team.getTeamId());
        }
        mentorRepository.deleteAllById(ids);
        return "Team Mentor Removed Successfully";
    }

    public String updateMentor(Long mentorId, Mentor updateMentor) {
        Optional<Mentor> mentor = mentorRepository.findById(mentorId);
        if (mentor.isEmpty()) {
            return "No Team mentor is present with given MentorId";
        }
        updateMentor.setMentorId(mentorId);
        mentorRepository.save(updateMentor);
        return "Team Mentor details updated successfully";
    }
}
