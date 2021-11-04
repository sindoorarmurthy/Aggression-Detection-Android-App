package com.sindoora.tantrum.Doctor;

public class Doctor {
    private String uid, firstName, lastName;
    private Boolean isDoctor = true;

    public Doctor() {

    }

    public Doctor(String uid, String firstName, String lastName, Boolean isDoctor) {
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isDoctor = isDoctor;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public Boolean getDoctor() {
        return isDoctor;
    }

    public void setDoctor(Boolean doctor) {
        isDoctor = doctor;
    }
}
