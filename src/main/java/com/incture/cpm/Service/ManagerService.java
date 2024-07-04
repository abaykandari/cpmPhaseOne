package com.incture.cpm.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.cpm.Entity.Manager;
import com.incture.cpm.Entity.Talent;
import com.incture.cpm.Entity.Team;
import com.incture.cpm.Exception.ResourceAlreadyExistsException;
import com.incture.cpm.Exception.ResourceNotFoundException;
import com.incture.cpm.Repo.ManagerRepository;
import com.incture.cpm.Repo.TalentRepository;
import com.incture.cpm.Repo.TeamRepository;

@Service
public class ManagerService {
     @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private TalentRepository talentRepository;

    @Autowired
    private TeamRepository teamRepository;

    public Optional<Manager> getManagerById(Long managerId){
        return managerRepository.findById(managerId);
    }

    public List<Manager> getAllManager(){
        return managerRepository.findAll();
    }

    public List<Manager> getManagerOfTeam(Long teamId){
        List<Manager> managerList= managerRepository.findByTeam(teamRepository.findById(teamId).get());
        if(managerList.isEmpty()){
            throw new ResourceNotFoundException("No Manager Present in the team with Id " +teamId );
        }
        return managerList;
    }

    public Manager addManager(Long talentId, Long teamId) {
        Optional<Manager> existingManager = managerRepository.findByTalentId(talentId);
        if (existingManager.isPresent()) {
            throw new ResourceAlreadyExistsException("Manager already exists in another team");
        }

        Optional<Talent> talent = talentRepository.findById(talentId);
        if (talent.isEmpty()) {
            throw new ResourceNotFoundException("No Talent exists with given Talent Id");
        }

        Optional<Team> team = teamRepository.findById(teamId);
        if (team.isEmpty()) {
            throw new ResourceNotFoundException("Incorrect Team Id given");
        }

        Manager createdManager = new Manager();
        createdManager.setTalentId(talentId);
        createdManager.setName(talent.get().getTalentName());
        createdManager.setTeam(team.get());
        return managerRepository.save(createdManager);
    }


    public String deleteManager(Long managerId){
        Optional<Manager> manager= managerRepository.findById(managerId);
        if(manager.isEmpty()){
            return "No Team member is present with given MemberId";
        }
        managerRepository.delete(manager.get());
        return "Team Member Removed Successfully";
    }

    public String updateManager(Long managerId,Manager updateManager){
        Optional<Manager> manager = managerRepository.findById(managerId);
        if (manager.isEmpty()) {
            return "No Team member is present with given MemberId";
        }
        updateManager.setManagerId(managerId);
        managerRepository.save(updateManager);
        return "Team Member details updated successfully";
    }
}
