package com.incture.cpm.Service;

import com.incture.cpm.Entity.Talent;
import com.incture.cpm.Entity.Trainer;
import com.incture.cpm.Entity.Training;
import com.incture.cpm.Exception.TrainerNotFoundException;
import com.incture.cpm.Repo.TalentRepository;
import com.incture.cpm.Repo.TrainerRepository;
import com.incture.cpm.Repo.TrainingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.support.ReflectivePropertyAccessor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class TrainingService {
    @Autowired
    private TrainingRepo trainingRepo;
    @Autowired
    private TrainerRepository trainerRepo;
    @Autowired
    private TalentRepository talentRepo;
    @Autowired
    private EmailSenderService emailSenderService;
    public Training insertFunction(Training training) {
        Long trainerId=training.getTrainerId();
        List<Long> talentIds=training.getTalentId();
        Optional<Trainer> trainer=trainerRepo.findById(trainerId);
        if(trainer.isPresent()){
            String trainerEmail=trainer.get().getEmail();
            String trainerEmailBody="Dear" + " " +  trainer.get().getTrainerName() + ",\n" +
                    "You have been scheduled as a trainer on the technology" + training.getTrainingTech() + "\n"
                    +"DateOfTraining" + training.getActualDateOfTraining() + "\n" +
                    "Thanks" + "\n" + "Regards"+ "\n" +  "Hanumant Rao Kulkarni";
            String  trainerEmailSubject="Training Schedule as a Trainer ";
            int size_of_talent= talentIds.size();
            for(int i=0; i<size_of_talent; i++){
                Optional<Talent> talent =talentRepo.findById(talentIds.get(i));
                String talentEmail=talent.get().getEmail();
                String talentEmailBody="Dear" + talent.get().getTalentName() + "\n" + "Your training Schedule for"
                        + training.getTrainingTech() + "is scheduled on" + ":\n" + "Date" + training.getActualDateOfTraining()
                        +"\n" + "TrainingTopic" + training.getTrainingTopic() + "\n" + "Trainer Name" + trainer.get().getTrainerName();
                String talentEmailSubject="Training Scheduled Details";
                emailSenderService.sendSimpleEmail(talentEmail, talentEmailSubject, talentEmailBody);
            }
            emailSenderService.sendSimpleEmail(trainerEmail, trainerEmailSubject, trainerEmailBody);
        }
        else{
            throw  new TrainerNotFoundException("");
        }

        return trainingRepo.save(training);
    }

    public List<Training> readFunction() {
        return trainingRepo.findAll();
    }
}
