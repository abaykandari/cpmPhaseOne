package com.incture.cpm.Dto;

import java.util.List;

import com.incture.cpm.Entity.AssessmentLevelTwo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AssessmentLevelTwoResponse {
    private Boolean isLevelTwoCompleted;
    private List<AssessmentLevelTwo> levelTwoList;
}
