/* package com.incture.cpm.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import com.incture.cpm.Entity.*;
import com.incture.cpm.Repo.AssessmentRepo;
import com.incture.cpm.Repo.CandidateRepository;
import com.incture.cpm.Repo.CollegeRepository;
import com.incture.cpm.Util.ExcelUtil;

@SpringBootTest
public class AssessmentServiceTest {

    @InjectMocks
    private AssessmentService assessmentService;

    @Mock
    private AssessmentRepo assessmentRepo;

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private TalentService talentService;

    @Mock
    private CollegeRepository collegeRepository;

    @Mock
    private ExcelUtil excelUtil;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllAssessments() {
        List<Assessment> assessments = new ArrayList<>();
        when(assessmentRepo.findAll()).thenReturn(assessments);

        Object result = assessmentService.getAllAssessments();
        assertEquals(assessments, result);
    }

    @Test
    public void testGetAssessmentByCollegeId() {
        int collegeId = 1;
        CollegeTPO college = new CollegeTPO();
        Assessment assessment = new Assessment();
        List<Assessment> assessments = Collections.singletonList(assessment);

        when(collegeRepository.findById(collegeId)).thenReturn(Optional.of(college));
        when(assessmentRepo.findAllByCollege(college)).thenReturn(Optional.of(assessments));

        List<Assessment> result = assessmentService.getAssessmentByCollegeId(collegeId);
        assertEquals(assessments, result);
    }

    @Test
    public void testLoadCandidates() {
        int collegeId = 1;
        Candidate candidate = new Candidate();
        candidate.setEmail("test@example.com");
        candidate.setCandidateName("Test Candidate");
        List<Candidate> candidates = Collections.singletonList(candidate);

        CollegeTPO college = new CollegeTPO();
        when(candidateRepository.findByCollegeId(collegeId)).thenReturn(candidates);
        when(collegeRepository.findById(collegeId)).thenReturn(Optional.of(college));
        when(assessmentRepo.findByEmail(candidate.getEmail())).thenReturn(Optional.empty());

        String result = assessmentService.loadCandidates(collegeId);
        assertEquals("Candidates Loaded Successfully", result);
        verify(assessmentRepo, times(1)).save(any(Assessment.class));
    }

    @Test
    public void testUpdateLevelOne() {
        AssessmentLevelOne levelOne = new AssessmentLevelOne();
        levelOne.setEmail("test@example.com");
        Assessment assessment = new Assessment();
        when(assessmentRepo.findByEmail(levelOne.getEmail())).thenReturn(Optional.of(assessment));

        String result = assessmentService.updateLevelOne(levelOne);
        assertEquals("Assessment Level One updated successfully", result);
        verify(assessmentRepo, times(1)).save(assessment);
    }

    @Test
    public void testUploadLevelOne() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        Map<String, String> data = new HashMap<>();
        data.put("candidateName", "Test Candidate");
        data.put("email", "test@example.com");
        data.put("quantitativeScore", "90");
        data.put("logicalScore", "85");
        data.put("verbalScore", "80");
        data.put("codingScore", "95");
        List<Map<String, String>> dataList = Collections.singletonList(data);

        when(excelUtil.readExcelFile(file)).thenReturn(dataList);
        CollegeTPO mockCollege = mock(CollegeTPO.class);
        when(collegeRepository.findById(anyInt())).thenReturn(Optional.of(mockCollege));
        
        Candidate candidate = new Candidate();
        candidate.setEmail("test@example.com");
        when(candidateRepository.findByEmail("test@example.com")).thenReturn(Optional.of(candidate));

        assessmentService.uploadLevelOne(file, 1);
        verify(assessmentRepo, times(1)).save(any(Assessment.class));
    }

    @Test
    public void testSelectLevelOne() {
        AssessmentLevelOne levelOne = new AssessmentLevelOne();
        levelOne.setEmail("test@example.com");
        Assessment assessment = new Assessment();
        when(assessmentRepo.findByEmail(levelOne.getEmail())).thenReturn(Optional.of(assessment));

        String result = assessmentService.selectLevelOne(Collections.singletonList(levelOne));
        assertEquals("Assessment Level Two saved successfully", result);
        verify(assessmentRepo, times(1)).save(assessment);
    }

    @Test
    public void testSelectLevelOne_AssessmentAlreadySelected() {
        AssessmentLevelOne levelOne = new AssessmentLevelOne();
        levelOne.setEmail("test@example.com");
        Assessment assessment = new Assessment();
        assessment.setAssessmentLevelTwo(new AssessmentLevelTwo());
        when(assessmentRepo.findByEmail(levelOne.getEmail())).thenReturn(Optional.of(assessment));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            assessmentService.selectLevelOne(Collections.singletonList(levelOne));
        });

        assertEquals("Candidate already selected", exception.getMessage());
    }

    // Additional tests for other methods
}
 */