package com.incture.cpm.Service;
import com.incture.cpm.Entity.Assignment;
import com.incture.cpm.Entity.EmployeewiseAssignment;
import com.incture.cpm.Entity.Talent;
import com.incture.cpm.Repo.AssignmentRepo;
import com.incture.cpm.Repo.EmployeewiseAssignmentRepo;
import com.incture.cpm.Repo.TalentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AssignmentService {
    @Autowired
    private AssignmentRepo assignmentRepo;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private TalentRepository talentRepository;
    @Autowired
    private EmployeewiseAssignmentRepo employeewiseAssignmentRepo;


    public Assignment createAssignment(Assignment assignment) {
        // Generate random assignment ID
        assignment.setAssignmentId(generateRandomInt());

        // Save the assignment
        Assignment savedAssignment = assignmentRepo.save(assignment);

        // Fetch talents based on assignedTo
        String assignedTo = assignment.getAssignedTo();
        String[] assignedToNames = assignedTo.split(",\\s*");

        for (String talentEmail : assignedToNames) {
            Talent talent = talentRepository.findByEmail(talentEmail).orElseThrow(() -> new IllegalStateException("Could not find talent"));
            System.out.println(talentEmail);
            if (talent != null) {
                EmployeewiseAssignment employeeAssignment = new EmployeewiseAssignment();
                employeeAssignment.setEmployeeAssignmentId(generateRandomInt());
                employeeAssignment.setEmployeeEmail(talentEmail);
                employeeAssignment.setAssignmentWeek(assignment.getAssignmentWeek());
                employeeAssignment.setAssignmentName(assignment.getAssignmentName());
                employeeAssignment.setAssignmentTechnology(assignment.getAssignmentTechnology());
                employeeAssignment.setAssignmentDuedate(assignment.getAssignmentDuedate());
                employeeAssignment.setAssignmentFileName(assignment.getAssignmentFileName());
                employeeAssignment.setAssignmentStatus("Pending");
                employeeAssignment.setAssignmentFileUrl(assignment.getAssignmentFileUrl());
                employeeAssignment.setEmployeeAssignmentScore(0);
                employeeAssignment.setMaxmarks(assignment.getMaxmarks());
                employeeAssignment.setEmployeeAssignmentFeedback("Not checked yet");
                employeeAssignment.setEmployeeAssignmentFileUrl("Not submitted yet");


                employeewiseAssignmentRepo.save(employeeAssignment);


                // Compose email body
                String emailBody = "Dear " + talent.getTalentName() + ",\n\n"
                        + "You have been assigned a new assignment. Please find the details below:\n\n"
                        + "Assignment Name: " + assignment.getAssignmentName() + "\n"
                        + "Due Date: " + assignment.getAssignmentDuedate() + "\n"
                        + "File Link: " + assignment.getAssignmentFileUrl() + "\n\n"
                        + "Best regards,\n"
                        + "Your Organization";

                // Send email to talent
                emailSenderService.sendSimpleEmail(talent.getEmail(), "New Assignment Assigned", emailBody);
            } else {
                System.out.println("Talent with email " + talent.getTalentName() + " not found.");
            }
        }

        return savedAssignment;
    }


    public List<Assignment> getAllAssignments() {
        return assignmentRepo.findAll();
    }

    public Optional<Assignment> getAssignmentById(int id) {

        return assignmentRepo.findById(id);

    }

//    public Assignment updateAssignment(int id, Assignment assignment,  MultipartFile assignmentFile) throws IOException {
//        if (assignmentRepo.existsById(id)) {
//            Assignment existingAssignment = assignmentRepo.findById(id).orElse(null);
//            if (existingAssignment != null) {
//                existingAssignment.setAssignmentName(assignment.getAssignmentName());
//                existingAssignment.setAssignmentWeek(assignment.getAssignmentWeek());
//                existingAssignment.setAssignmentDate(assignment.getAssignmentDate());
//                existingAssignment.setAssignmentScore(assignment.getAssignmentScore());
//                existingAssignment.setAssignmentStatus(assignment.getAssignmentStatus());
//                existingAssignment.setAssignmentFile(assignmentFile.getBytes());
//                return assignmentRepo.save(existingAssignment);
//            }
//        }
//        return null;
//
//    }

    public void deleteAssignmentById(int id) {
        assignmentRepo.deleteById(id);

    }
    private int generateRandomInt() {
        Random random = new Random();
        return random.nextInt(Integer.MAX_VALUE);
    }
    public Assignment updateAssignment(int id, Assignment assignment) {
        Optional<Assignment> existingAssignmentOptional = assignmentRepo.findById(id);

        if (existingAssignmentOptional.isPresent()) {
            Assignment existingAssignment = existingAssignmentOptional.get();

            // Update assignment properties
            existingAssignment.setAssignmentWeek(assignment.getAssignmentWeek());
            existingAssignment.setAssignmentName(assignment.getAssignmentName());
            existingAssignment.setAssignmentTechnology(assignment.getAssignmentTechnology());
            existingAssignment.setAssignmentDuedate(assignment.getAssignmentDuedate());
            existingAssignment.setAssignedTo(assignment.getAssignedTo());

            // Save and return updated assignment
            return assignmentRepo.save(existingAssignment);
        } else {
            // Handle case when assignment with given id is not found
            throw new RuntimeException("Assignment not found with id: " + id);
        }
    }
}
