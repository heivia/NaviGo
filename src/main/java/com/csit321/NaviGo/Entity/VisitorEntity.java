package com.csit321.NaviGo.Entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "Visitor")
public class VisitorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "card_no")
    private int cardNo;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "time_in")
    private String timeIn;

    @Column(name = "time_out")
    private String timeOut;

    @Column(name = "purpose")
    private String purpose;

    @Column(name = "building_to_visit")
    private String buildingToVisit;

    @Column(name = "office_visited")
    private String officeVisited;
    
    @Column(name = "assigned_person")
    private String assignedPerson;

    @Column(name = "status")
    private int status;

    @JsonProperty("selected_gate")
    @Column(name = "selected_gate")
    private String selectedGate;

    @Column(name = "photo")
    private int photo;

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCardNo() {
        return cardNo;
    }

    public void setCardNo(int cardNo) {
        this.cardNo = cardNo;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getBuildingToVisit() {
        return buildingToVisit;
    }

    public void setBuildingToVisit(String buildingToVisit) {
        this.buildingToVisit = buildingToVisit;
    }

    public String getOfficeVisited() {
        return officeVisited;
    }

    public void setOfficeVisited(String officeVisited) {
        this.officeVisited = officeVisited;
    }
    
    public String getassignedPerson() {
        return assignedPerson;
    }

    public void setassignedPerson(String assignedPerson) {
        this.assignedPerson = assignedPerson;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSelectedGate() {
        return selectedGate;
    }

    public void setSelectedGate(String selectedGate) {
        this.selectedGate = selectedGate;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }
}
