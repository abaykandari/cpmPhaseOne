package com.incture.cpm.Dto;

import java.util.List;

import com.incture.cpm.Entity.AssessmentLevelThree;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AssessmentLevelThreeResponse {
    private Boolean isLevelThreeCompleted;
    private List<AssessmentLevelThree> levelThreeList;
}
