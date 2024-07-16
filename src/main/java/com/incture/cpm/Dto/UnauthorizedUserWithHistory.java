package com.incture.cpm.Dto;

import java.util.List;

import com.incture.cpm.Entity.History;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnauthorizedUserWithHistory {
    private UserDto userDto;
    private List<History> history;    

    public UnauthorizedUserWithHistory(UserDto userDto, List<History> history) {
        this.userDto = userDto;
        this.history = history;
    }
}
