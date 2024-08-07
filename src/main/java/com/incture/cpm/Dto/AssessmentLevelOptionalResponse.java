package com.incture.cpm.Dto;

import java.util.List;

import com.incture.cpm.Entity.AssessmentLevelOptional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AssessmentLevelOptionalResponse {
    private Boolean isLevelOptionalCompleted;
    private List<AssessmentLevelOptional> levelOptionalList;
}
