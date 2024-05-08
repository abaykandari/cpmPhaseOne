package com.example.cpm.Entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Regularize {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long empId;
    private Date checkIn;
    private Date checkOut;
    private String approvalManager;
    private String actions;

    // Constructors, Getters, and Setters
    public Regularize() {
    }

    public Regularize(Date checkIn, Date checkOut, String approvalManager, String actions) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.approvalManager = approvalManager;
        this.actions = actions;
    }

    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    public Date getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Date checkIn) {
        this.checkIn = checkIn;
    }

    public Date getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(Date checkOut) {
        this.checkOut = checkOut;
    }

    public String getApprovalManager() {
        return approvalManager;
    }

    public void setApprovalManager(String approvalManager) {
        this.approvalManager = approvalManager;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }
}

