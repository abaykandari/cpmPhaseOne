package com.incture.cpm.Dto;

import java.util.List;

import com.incture.cpm.Entity.History;
import com.incture.cpm.Entity.UnauthorizedUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnauthorizedUserWithHistory {
    private UnauthorizedUser user;
    private List<History> history;    

    public UnauthorizedUserWithHistory(UnauthorizedUser user, List<History> history) {
        this.user = user;
        this.history = history;
    }
}
