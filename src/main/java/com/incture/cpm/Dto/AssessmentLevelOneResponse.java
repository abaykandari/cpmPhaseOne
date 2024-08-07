package com.incture.cpm.Dto;

import java.util.List;

import com.incture.cpm.Entity.AssessmentLevelOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AssessmentLevelOneResponse {
    private Boolean isLevelOneCompleted;
    private List<AssessmentLevelOne> levelOneList;
}
