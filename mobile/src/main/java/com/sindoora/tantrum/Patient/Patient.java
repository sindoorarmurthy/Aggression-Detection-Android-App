package com.sindoora.tantrum.Patient;

public class Patient {
    private String uid, firstName, lastName, age, heartRate, temperature, bloodPressure, diagnosis;
    private Boolean isDoctor = false;

    public Patient() {

    }

    public Patient(String uid, String firstName, String lastName, Boolean isDoctor) {
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isDoctor = isDoctor;
    }

    public Patient(String uid, String firstName, String lastName, String age, String heartRate, String temperature, String bloodPressure, Boolean isDoctor) {
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.heartRate = heartRate;
        this.temperature = temperature;
        this.bloodPressure = bloodPressure;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public Boolean getDoctor() { return isDoctor;
    }

    public void setDoctor(Boolean doctor) {
        isDoctor = doctor;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }
}
