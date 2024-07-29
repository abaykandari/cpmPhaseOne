package com.incture.cpm.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

import com.incture.cpm.Entity.History;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UnauthorizedUserDto {
    private Long id;
    private String email;
    private Set<String> roles;
    private String talentName;
    private String inctureId;
    private String status;
    private List<History> authenticationHistory;

}
