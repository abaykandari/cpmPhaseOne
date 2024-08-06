package com.incture.cpm.Dto;

public interface AssessmentProjection {
    Long getAssessmentId();
    String getEkYear();
    Boolean getIsLevelOneCompleted();
    Boolean getIsLevelTwoCompleted();
    Boolean getIsLevelThreeCompleted();
    Boolean getIsLevelOptionalCompleted();
    int getCollegeId();
    String getCollegeName();
}
