package com.incture.cpm.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArchievedColleges {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String collegeName;
    @Column
    private String tpoName;
    @Column
    private String primaryEmail;
    @Column
    private String phoneNumber;
    @Column
    private String addressLine1;
    @Column
    private String addressLine2;
    @Column
    private String location;
    @Column
    private String region;
    @Column
    private String collegeOwner;
    @Column
    private String primaryContact;
    @Column
    private String secondaryContact;
    @Column
    private String tier;
    @Column
    private String pinCode;
    @Column
    private String state;
    @Column
    private double compensation;

}
