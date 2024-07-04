package com.incture.cpm.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TalentSummaryDto {
    private int totalTalents;
    private int activeTalents;
    private int inactiveTalents;
    private int declinedTalents;
    private int resignedTalents;
    private int revokedTalents;
}
